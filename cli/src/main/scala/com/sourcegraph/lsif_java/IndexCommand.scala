package com.sourcegraph.lsif_java

import moped.cli.{Application, Command, CommandParser}
import moped.annotations.Description

import java.nio.file.{Files, Path, Paths}

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

  override def run(): Int = ???
}

object IndexCommand {
  implicit val parser: CommandParser[IndexCommand] = CommandParser
    .derive(IndexCommand())
}
