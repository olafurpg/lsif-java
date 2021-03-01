package com.sourcegraph.lsif_java.buildtools

import com.sourcegraph.lsif_java.IndexCommand

abstract class BuildTool(val name: String) {

  def usedInCurrentDirectory(): Boolean

  def generateSemanticdb(): os.CommandResult

}

object BuildTool {
  def all(index: IndexCommand): List[BuildTool] =
    List(new GradleBuildTool(index), new MavenBuildTool(index))
}
