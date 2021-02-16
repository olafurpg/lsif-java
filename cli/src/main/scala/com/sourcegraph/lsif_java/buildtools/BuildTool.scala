package com.sourcegraph.lsif_java.buildtools

import com.sourcegraph.lsif_java.IndexCommand

abstract class BuildTool(val name: String, index: IndexCommand) {

  def exists(): Boolean

  def generateSemanticdb(): os.CommandResult

  final def generateLsif(): os.CommandResult = {
    index
      .app
      .process(
        "lsif-semanticdb",
        s"--semanticdbDir=${index.targetrootAbsolutePath}"
      )
      .call(check = true)
  }

}

object BuildTool {
  def all(index: IndexCommand): List[BuildTool] =
    List(new GradleBuildTool(index), new MavenBuildTool(index))
}
