package com.sourcegraph.lsif_java

abstract class BuildTool(index: IndexCommand) {
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
