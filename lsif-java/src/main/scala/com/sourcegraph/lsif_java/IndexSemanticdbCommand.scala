package com.sourcegraph.lsif_java

import com.sourcegraph.lsif_protocol.LsifToolInfo
import com.sourcegraph.lsif_semanticdb.{LsifSemanticdb, LsifSemanticdbOptions}

import scala.jdk.CollectionConverters._
import java.nio.file.{Path, Paths}
import scala.collection.mutable.ListBuffer
import moped.annotations.CommandName
import moped.annotations.Description
import moped.annotations.ExampleUsage
import moped.annotations.Inline
import moped.annotations.PositionalArguments
import moped.annotations.Usage
import moped.cli.Application
import moped.cli.Command
import moped.cli.CommandParser
import os.Inherit
import os.Shellable

@Description("Converts SemanticDB files into a single LSIF index file.")
@Usage("lsif-java index-semanticdb [OPTIONS ...] [POSITIONAL ARGUMENTS ...]")
@ExampleUsage(
  "lsif-java index-semanticdb --out=myindex.lsif my/targetroot1 my/targetroot2"
)
@CommandName("index-semanticdb")
final case class IndexSemanticdbCommand(
    @Description("The name of the output file.") output: Path = Paths
      .get("dump.lsif"),
    @Description("Directories that contain SemanticDB files.")
    @PositionalArguments() targetroots: List[Path] = Nil,
    @Inline() app: Application = Application.default
) extends Command {
  def sourceroot: Path = AbsolutePath.of(app.env.workingDirectory)
  def run(): Int = {
    val reporter = new MopedLsifReporter(app)
    val options =
      new LsifSemanticdbOptions(
        targetroots.map(ts => AbsolutePath.of(ts, sourceroot)).asJava,
        AbsolutePath.of(output, sourceroot),
        sourceroot,
        reporter,
        new LsifToolInfo("lsif-java", BuildInfo.version, Array()),
        "java"
      )
    LsifSemanticdb.run(options)
    app.info(options.output.toString)
    app.reporter.exitCode()
  }
}

object IndexSemanticdbCommand {
  val default = IndexSemanticdbCommand()
  implicit val parser = CommandParser.derive(default)
}
