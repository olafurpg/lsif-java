package com.sourcegraph.lsif_java
import os.{CommandResult, Inherit}

import java.nio.file.Files

class MavenBuildTool(index: IndexCommand) extends BuildTool("Maven", index) {
  override def exists(): Boolean =
    Files.isRegularFile(index.workspace.resolve("pom.xml"))

  override def bloopInstall(): CommandResult = {
    index
      .app
      .process(
        "mvn",
        s"ch.epfl.scala:maven-bloop_2.12:${BuildInfo.bloopVersion}:bloopInstall",
        "-DdownloadSources=true"
      )
      .call(stdout = Inherit, stderr = Inherit)
  }
}
