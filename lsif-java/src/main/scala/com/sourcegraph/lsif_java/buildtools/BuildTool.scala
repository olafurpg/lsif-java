package com.sourcegraph.lsif_java.buildtools

import java.nio.file.Path

import com.sourcegraph.io.AbsolutePath
import com.sourcegraph.lsif_java.commands.IndexCommand

/**
 * A build tool such as Gradle, Maven or Bazel.
 */
abstract class BuildTool(val name: String, index: IndexCommand) {

  protected def defaultTargetroot: Path

  def isHidden: Boolean = false
  def buildKind: String = ""

  final def sourceroot: Path = index.workingDirectory
  final def targetroot: Path =
    AbsolutePath
      .of(index.targetroot.getOrElse(defaultTargetroot), index.workingDirectory)

  def usedInCurrentDirectory(): Boolean

  def generateSemanticdb(): os.CommandResult

}

object BuildTool {
  def all(index: IndexCommand): List[BuildTool] =
    List(
      new GradleBuildTool(index),
      new MavenBuildTool(index),
      new LsifBuildTool(index)
    )
  def allNames: String =
    all(IndexCommand()).filterNot(_.isHidden).map(_.name).mkString(", ")
}
