package com.sourcegraph.lsif_java

import ch.epfl.scala.bsp4j.StatusCode
import moped.cli.{Application, Command, CommandParser}
import moped.annotations.{Description, Inline, TrailingArguments}
import os.Shellable
import sun.jvmstat.monitor.event.MonitorStatusChangeEvent

import java.nio.file.{Files, Path, Paths}
import scala.collection.mutable.ListBuffer

@Description(
  "Generates an LSIF index for the Java build of a provided workspace directory."
)
case class IndexCommand(
    @Description("The path where to generate the LSIF index.") output: Path =
      Paths.get("dump.lsif"),
    @Description(
      "The directory where to generate SemanticDB files."
    ) targetroot: Path = Paths.get("semanticdb-targetroot"),
    @Description(
      "Whether to enable the -verbose flag in the SemanticDB compiler plugin."
    ) verbose: Boolean = false,
    @Description(
      "Whether to enable the -text:on flag in the SemanticDB compiler plugin."
    ) text: Boolean = false,
    @Description(
      "The version of the SemanticDB compiler plugin that should be automatically installed."
    ) semanticdbVersion: Option[String] = None,
    @Description(
      "Whether Bloop has already been installed in the workspace."
    ) bloopInstalled: Boolean = false,
    @TrailingArguments
    buildCommand: List[String] = Nil,
    @Inline
    app: Application = Application.default
) extends Command {

  def targetrootAbsolutePath: Path =
    if (targetroot.isAbsolute)
      targetroot
    else
      workspace.resolve(targetroot)
  def textFlag: String =
    if (text)
      "-text:on"
    else
      ""
  def verboseFlag: String =
    if (verbose)
      "-verbose:on"
    else
      ""

  def workspace: Path = app.env.workingDirectory

  override def run(): Int = {
    BuildTool.all(this).filter(_.exists()) match {
      case Nil =>
        app.error(
          "No build tool detected. To fix this problem, you need to generate LSIF manually."
        )
        1
      case tool :: Nil =>
        val semanticdb = tool.bloopInstall()
        if (semanticdb.exitCode != 0)
          semanticdb.exitCode
        else {
          val bloop = BloopClient.create(workspace, app.reporter)
          bloop.compileAll().getStatusCode match {
            case StatusCode.ERROR =>
              app.error("Compilation failed.")
              1
            case _ =>
              val arguments = ListBuffer.empty[String]
              arguments += "lsif-semanticdb"
              bloop
                .semanticdbDirectories()
                .foreach { dir =>
                  arguments += s"--semanticdbDir=$dir"
                }
              app.process(Shellable(arguments)).call(check = false).exitCode
          }
        }
      case many =>
        val names = many.map(_.name).mkString(", ")
        app.error(
          s"Multiple build tools detected: $names. To fix this problem ???"
        )
        1
    }
  }
}

object IndexCommand {
  val default = IndexCommand()
  implicit val parser: CommandParser[IndexCommand] = CommandParser
    .derive(default)
}
