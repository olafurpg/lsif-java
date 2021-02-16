package com.sourcegraph.lsif_java

import bloop.bloopgun.core.Shell
import bloop.launcher.LauncherMain
import ch.epfl.scala.bsp4j._
import com.sourcegraph.lsif_java.BloopClient._
import moped.reporters.Reporter
import org.eclipse.lsp4j.jsonrpc.{JsonRpcException, Launcher}

import java.io._
import java.nio.channels.{Channels, Pipe}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, StandardOpenOption}
import java.util.concurrent.{ConcurrentLinkedQueue, TimeUnit}
import scala.concurrent.{Await, ExecutionContext, Promise}
import scala.concurrent.duration.Duration
import scala.jdk.CollectionConverters._

class BloopClient(
    val client: MdocBuildClient,
    val server: BloopBuildServer,
    val initialize: InitializeBuildResult,
    val close: () => Unit
) {
  def compileAll(): CompileResult = {
    val targets = server.workspaceBuildTargets().get()
    val ids = targets.getTargets.asScala.map(_.getId).asJava
    server.buildTargetCompile(new CompileParams(ids)).get()
  }
  def semanticdbDirectories(): List[Path] = Nil
}

object BloopClient {
  def create(workspace: Path, reporter: Reporter): BloopClient = {
    val launcherInOutPipe = Pipe.open()
    val launcherIn =
      new QuietInputStream(
        Channels.newInputStream(launcherInOutPipe.source()),
        "Bloop InputStream"
      )
    val clientOut =
      new ClosableOutputStream(
        Channels.newOutputStream(launcherInOutPipe.sink()),
        "Bloop OutputStream"
      )
    val clientInOutPipe = Pipe.open()
    val clientIn = Channels.newInputStream(clientInOutPipe.source())
    val launcherOut = Channels.newOutputStream(clientInOutPipe.sink())
    val serverStarted = Promise[Unit]()
    val devnull =
      new PrintStream(
        new OutputStream {
          def write(b: Int): Unit = ()
        }
      )
    val main =
      new LauncherMain(
        launcherIn,
        launcherOut,
        devnull,
        StandardCharsets.UTF_8,
        Shell.default,
        userNailgunHost = None,
        userNailgunPort = None,
        serverStarted
      )

    ExecutionContext
      .global
      .execute(() =>
        main.runLauncher(
          BuildInfo.bloopVersion,
          skipBspConnection = false,
          serverJvmOptions = Nil
        )
      )
    Await.result(serverStarted.future, Duration(1, TimeUnit.MINUTES))
    val tracePath = workspace.resolve("bsp.trace.json")
    val fos = Files.newOutputStream(
      tracePath,
      StandardOpenOption.CREATE,
      StandardOpenOption
        .TRUNCATE_EXISTING // don't append infinitely to existing file
    )
    val tracer = new PrintWriter(fos)
    val localClient = new MdocBuildClient(reporter)
    val launcher = new Launcher.Builder[BloopBuildServer]()
      .traceMessages(tracer)
      .setOutput(clientOut)
      .setInput(clientIn)
      .setLocalService(localClient)
      .setRemoteInterface(classOf[BloopBuildServer])
      // .setExecutorService(ec)
      .create()
    val listening = launcher.startListening()
    val server = launcher.getRemoteProxy
    val initializeResult = server
      .buildInitialize(
        new InitializeBuildParams(
          "lsif-java",
          BuildInfo.version,
          BuildInfo.bspVersion,
          workspace.toString,
          new BuildClientCapabilities(List("scala", "java").asJava)
        )
      )
      .get()
    server.onBuildInitialized()
    new BloopClient(
      localClient,
      server,
      initializeResult,
      close =
        () => {
          listening.cancel(false)
        }
    )
  }

  trait BloopBuildServer extends BuildServer with ScalaBuildServer
  class MdocBuildClient(reporter: Reporter) extends BuildClient {
    val diagnostics = new ConcurrentLinkedQueue[PublishDiagnosticsParams]
    import scala.collection.JavaConverters._
    def hasDiagnosticSeverity(severity: DiagnosticSeverity): Boolean =
      diagnostics
        .asScala
        .exists(_.getDiagnostics.asScala.exists(_.getSeverity == severity))
    override def onBuildShowMessage(params: ShowMessageParams): Unit = ()
    override def onBuildLogMessage(params: LogMessageParams): Unit = {
      params.getType match {
        case MessageType.ERROR =>
          reporter.error(params.getMessage)
        case MessageType.WARNING =>
          reporter.warning(params.getMessage)
        case MessageType.INFORMATION =>
          reporter.info(params.getMessage)
        case MessageType.LOG =>
          reporter.debug(params.getMessage)
      }
    }
    override def onBuildTaskStart(params: TaskStartParams): Unit = ()
    override def onBuildTaskProgress(params: TaskProgressParams): Unit = ()
    override def onBuildTaskFinish(params: TaskFinishParams): Unit = ()
    override def onBuildPublishDiagnostics(
        params: PublishDiagnosticsParams
    ): Unit = {
      diagnostics.add(params)
      pprint.log(params)
    }
    override def onBuildTargetDidChange(params: DidChangeBuildTarget): Unit = ()
  }

  private class QuietInputStream(underlying: InputStream, name: String)
      extends FilterInputStream(underlying) {
    override def read(): Int = {
      try {
        underlying.read()
      } catch {
        case e: IOException =>
          -1
      }
    }
  }

  private class ClosableOutputStream(underlying: OutputStream, name: String)
      extends FilterOutputStream(underlying) {
    var isClosed = false
    override def flush(): Unit = {
      try {
        if (!isClosed) {
          super.flush()
        }
      } catch {
        case _: IOException =>
      }
    }
    override def write(b: Int): Unit = {
      try {
        if (!isClosed) {
          underlying.write(b)
        }
      } catch {
        // IOException is usually thrown when the stream is closed
        case e @ (_: IOException | _: JsonRpcException) =>
          isClosed = true
          throw e
      }
    }
  }
}
