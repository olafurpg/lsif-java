package com.sourcegraph.lsif_java.buildtools

import java.nio.file.Files

import com.sourcegraph.lsif_java.Embedded
import com.sourcegraph.lsif_java.IndexCommand
import os.CommandResult
import os.Inherit

class MavenBuildTool(index: IndexCommand) extends BuildTool("Maven") {
  override def usedInCurrentDirectory(): Boolean =
    Files.isRegularFile(index.workspace.resolve("pom.xml"))

  override def generateSemanticdb(): CommandResult = {
    TemporaryFiles.withDirectory(index.cleanup) { tmp =>
      val mvnw = index.workspace.resolve("mvnw")
      val mavenScript =
        if (Files.isRegularFile(mvnw))
          mvnw.toString
        else
          "mvn"
      index.process(
        mavenScript,
        //        "-X",
        s"-Dmaven.compiler.useIncrementalCompilation=false",
        // NOTE(olafur) the square/javapoet repo sets compilerId to 'javac-with-javac', which appears to
        // override the '-Dmaven.compiler.executable' setting.. Forcing the compilerId to 'javac' fixes the
        // issue for this repo.
        s"-Dmaven.compiler.compilerId=javac",
        s"-Dmaven.compiler.executable=${Embedded
          .customJavac(index.sourceroot, index.targetrootAbsolutePath, tmp)}",
        s"-Dmaven.compiler.fork=true",
        s"clean",
        // Default to the "verify" command, as recommended by the official docs
        // https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#usual-command-line-calls
        "verify",
        "-DskipTests"
      )
    }
  }
}
