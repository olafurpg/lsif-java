package com.sourcegraph.lsif_java

import os.CommandResult

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}
import scala.util.Properties

class GradleBuildTool(index: IndexCommand) extends BuildTool(index) {

  private val gradleWrapper: Path = index
    .workspace
    .resolve(
      if (Properties.isWin)
        "gradlew.bat"
      else
        "gradlew"
    )

  override def exists(): Boolean = {
    Files.isRegularFile(index.workspace.resolve("build.gradle")) ||
    Files.isRegularFile(index.workspace.resolve("build.gradle.kts"))
  }

  private def withInitScript[T](fn: Path => T): T = {
    val tmp = Files.createTempFile("lsif-java", "init-script.gradle")
    val annotationProcessor =
      s"com.sourcegraph:semanticdb-javac:${BuildInfo.version}"
    val compilerArgs: String = List(
      "-Xplugin:semanticdb",
      s"-targetroot:${index.targetrootAbsolutePath}",
      s"-sourceroot:${index.workspace}",
      index.verboseFlag,
      index.textFlag
    ).filter(_.nonEmpty).mkString(" ")
    val script =
      s"""|allprojects {
          |  dependencies {
          |    annotationProcessor "$annotationProcessor"
          |  }
          |  tasks.withType(JavaCompile) {
          |    options.compilerArgs << "$compilerArgs"
          |  }
          |}
          |""".stripMargin

    Files.write(tmp, script.getBytes(StandardCharsets.UTF_8))
    try fn(tmp)
    finally Files.deleteIfExists(tmp)
  }

  override def generateSemanticdb(): CommandResult = {
    withInitScript { initScript =>
      index
        .app
        .process(
          gradleWrapper.toString,
          "--console=plain",
          "--init-script",
          initScript.toString,
          "compileJava"
        )
        .call(check = false)
    }
  }
}
