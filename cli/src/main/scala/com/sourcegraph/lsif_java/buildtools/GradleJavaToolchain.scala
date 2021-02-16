package com.sourcegraph.lsif_java.buildtools

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

import scala.collection.mutable.ListBuffer

import com.sourcegraph.lsif_java.Embedded
import com.sourcegraph.lsif_java.IndexCommand

case class GradleJavaToolchain(version: String, javacPath: Path) {

  def createForwardingToolchain(
      index: IndexCommand,
      tmp: Path,
      processorPath: Path
  ): Path = {
    val home = tmp.resolve(s"1.$version")
    val javac = Embedded
      .customJavac(index.sourceroot, index.targetrootAbsolutePath, tmp)
    val agent = Embedded.agentJar(tmp)
    val debugPath = GradleJavaToolchain.debugPath(tmp)

    createBinaries(home, javac, agent, index, processorPath, debugPath)
    createBinaries(
      home.resolve("Contents").resolve("Home"),
      javac,
      agent,
      index,
      processorPath,
      debugPath
    ) // for macOS
    home
  }

  private def createBinaries(
      dir: Path,
      javac: Path,
      agent: Path,
      index: IndexCommand,
      processorPath: Path,
      debugPath: Path
  ): Unit = {
    Files.createDirectories(dir.resolve("bin"))
    val javaBinary = javacPath.resolveSibling("java")
    val javaCommand = ListBuffer[String](
      javaBinary.toString,
      s"-javaagent:$agent",
      s"-Dsemanticdb.processorpath=${processorPath}",
      s"-Dsemanticdb.targetroot=${index.targetrootAbsolutePath}",
      s"-Dsemanticdb.sourceroot=${index.sourceroot}"
    )
    if (index.verbose) {
      javaCommand += s"-Dsemanticdb.debugpath=${debugPath}"
    }
    javaCommand += """"$@""""
    Files
      .write(
        dir.resolve("bin").resolve("java"),
        s"""#!/bin/sh
           |set -eu
           |echo $$@ >> ${dir.resolve("java_arguments")}
           |${javaCommand.mkString(" ")}
           |""".stripMargin.getBytes(StandardCharsets.UTF_8)
      )
      .toFile
      .setExecutable(true)
    Files
      .copy(
        javac,
        dir.resolve("bin").resolve("javac"),
        StandardCopyOption.REPLACE_EXISTING
      )
      .toFile
      .setExecutable(true)
  }
}

object GradleJavaToolchain {
  def debugPath(tmp: Path): Path = tmp.resolve("debugpath.txt")
  def fromLine(line: String): Option[GradleJavaToolchain] =
    line.split(' ') match {
      case Array(version, path) =>
        Some(GradleJavaToolchain(version, Paths.get(path)))
      case _ =>
        None
    }
}
