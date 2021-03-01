package com.sourcegraph.lsif_java

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.sourcegraph.lsif_java.buildtools.BuildTool
import moped.annotations.Description
import moped.annotations.Inline
import moped.cli.Application
import moped.cli.Command
import moped.cli.CommandParser
import os.CommandResult
import os.Inherit
import os.Shellable

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
      "Whether to enable remove generated temporary files on exit."
    ) cleanup: Boolean = true,
    @Inline
    app: Application = Application.default
) extends Command {

  def process(shellable: String*): CommandResult = {
    app.info(shellable.mkString(" "))
    app
      .process(Shellable(shellable))
      .call(check = false, stdout = Inherit, stderr = Inherit)
  }

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
  def sourceroot: Path = workspace

  override def run(): Int = {
    val allBuildTools = BuildTool.all(this)

    allBuildTools.filter(_.usedInCurrentDirectory()) match {
      case Nil =>
        if (Files.isDirectory(workspace)) {
          val buildToolNames = allBuildTools.map(_.name).mkString(", ")
          app.error(
            s"No build tool detected in workspace '$workspace'. " +
              s"At the moment, the only supported build tools are: $buildToolNames. " +
              s"To fix this problem, you will need to generate the LSIF manually."
          )
        } else {
          val cause =
            if (Files.exists(workspace)) {
              s"Workspace '$workspace' is not a directory" + s""
            } else {
              s"The directory '$workspace' does not exist"
            }
          app.error(
            cause +
              s". To fix this problem, make sure the working directory is an actual directory."
          )
        }
        1
      case tool :: Nil =>
        val generateSemanticdb = tool.generateSemanticdb()
        if (!Files.isDirectory(targetrootAbsolutePath)) {
          generateSemanticdb.exitCode
        } else {
          val generateLsif = process(
            "lsif-semanticdb",
            s"--semanticdbDir=$targetrootAbsolutePath"
          )
          generateSemanticdb.exitCode + generateLsif.exitCode
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
