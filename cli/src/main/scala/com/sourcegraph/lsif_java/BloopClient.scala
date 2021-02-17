package com.sourcegraph.lsif_java

import ch.epfl.scala.bsp4j._
import com.sourcegraph.lsif_java.BloopClient._
import moped.reporters.Reporter
import org.eclipse.lsp4j.jsonrpc.{JsonRpcException, Launcher}

import java.io._
import java.lang.ProcessBuilder.Redirect
import java.nio.file.{Files, Path, StandardOpenOption}
import java.util.concurrent.{ConcurrentLinkedQueue, Executors}
import scala.jdk.CollectionConverters._

class BloopClient(
    val client: LsifBuildClient,
    val server: BloopBuildServer,
    val initialize: InitializeBuildResult,
    val close: () => Unit
) {
  def compileAll(): CompileResult = {
    val targets = server.workspaceBuildTargets().get()
    val ids = targets.getTargets.asScala.map(_.getId).asJava
    server.buildTargetCompile(new CompileParams(ids)).get()
  }
  def exit(): Unit = {
    server.buildShutdown().get()
    server.onBuildExit()
    close()
  }
  def semanticdbDirectories(index: IndexCommand): List[Path] =
    List(index.targetrootAbsolutePath)
}

object BloopClient {
  def create(workspace: Path, reporter: Reporter): BloopClient = {
    val launcherClasspath = Jars
      .fetch(
        s"ch.epfl.scala:bloop-launcher-core_2.12:${BuildInfo.bloopVersion}"
      )
      .mkString(File.pathSeparator)
    val commands = List(
      "java",
      "-cp",
      launcherClasspath,
      "bloop.launcher.Launcher",
      BuildInfo.bloopVersion
    )
    val proc = new ProcessBuilder(commands: _*)
      .directory(workspace.toFile)
      .start()
    val tracePath = workspace.resolve("bsp.trace.json")
    val fos = Files.newOutputStream(
      tracePath,
      StandardOpenOption.CREATE,
      // don't append infinitely to existing file
      StandardOpenOption.TRUNCATE_EXISTING
    )
    val tracer = new PrintWriter(fos)
    val localClient = new LsifBuildClient(reporter)
    val ec = Executors.newCachedThreadPool()
    val launcher = new Launcher.Builder[BloopBuildServer]()
      .traceMessages(tracer)
      .setOutput(proc.getOutputStream)
      .setInput(proc.getInputStream)
      .setLocalService(localClient)
      .setRemoteInterface(classOf[BloopBuildServer])
      .setExecutorService(ec)
      .create()
    val listening = launcher.startListening()
    val server = launcher.getRemoteProxy
    val initializeResult = server
      .buildInitialize(
        new InitializeBuildParams(
          "lsif-java",
          BuildInfo.version,
          BuildInfo.bspVersion,
          workspace.toUri.toString,
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
          proc.destroy()
          listening.cancel(false)
        }
    )
  }

  trait BloopBuildServer extends BuildServer with ScalaBuildServer
  class LsifBuildClient(reporter: Reporter) extends BuildClient {
    val diagnostics = new ConcurrentLinkedQueue[PublishDiagnosticsParams]
    import scala.collection.JavaConverters._
    def hasDiagnosticSeverity(severity: DiagnosticSeverity): Boolean =
      diagnostics
        .asScala
        .exists(_.getDiagnostics.asScala.exists(_.getSeverity == severity))
    override def onBuildShowMessage(params: ShowMessageParams): Unit = {}
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
