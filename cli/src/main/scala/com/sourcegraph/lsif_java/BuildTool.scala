package com.sourcegraph.lsif_java

abstract class BuildTool(val name: String, index: IndexCommand) {

  def exists(): Boolean

  def bloopInstall(): os.CommandResult

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
    List(new GradleBuildTool(index))
}
