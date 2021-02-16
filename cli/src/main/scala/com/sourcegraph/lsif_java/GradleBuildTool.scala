package com.sourcegraph.lsif_java

import os.{CommandResult, Shellable}

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}
import scala.collection.mutable.ListBuffer
import scala.util.Properties

class GradleBuildTool(index: IndexCommand) extends BuildTool("Gradle", index) {

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

  override def bloopInstall(): CommandResult = {
    withInitScript { initScript =>
      index
        .app
        .process(
          gradleWrapper.toString,
          "--console=plain",
          "--init-script",
          initScript.toString,
          "bloopInstall"
        )
        .call(check = false)
    }
  }

  val annotationProcessor =
    s"com.sourcegraph:semanticdb-javac:${index.semanticdbVersion.getOrElse(BuildInfo.version)}"
  val compilerArgs: String = List(
    "-Xplugin:semanticdb",
    s"-targetroot:${index.targetrootAbsolutePath}",
    s"-sourceroot:${index.workspace}",
    index.verboseFlag,
    index.textFlag
  ).filter(_.nonEmpty).mkString(" ")

  private def withInitScript[T](fn: Path => T): T = {
    val tmp = Files.createTempFile("lsif-java", "init-script.gradle")
    val gradleBloop =
      s"ch.epfl.scala:gradle-bloop_2.12:${BuildInfo.bloopVersion}"
    val script =
      s"""
         |initscript {
         |  repositories{
         |    mavenCentral()
         |  }
         |  dependencies {
         |    classpath '$gradleBloop'
         |  }
         |}
         |allprojects {
         |  apply plugin: bloop.integrations.gradle.BloopPlugin
         |}
    """.stripMargin
    Files.write(tmp, script.getBytes(StandardCharsets.UTF_8))
    try fn(tmp)
    finally Files.deleteIfExists(tmp)
  }

}
