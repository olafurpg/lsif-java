package com.sourcegraph.lsif_java

import bloop.config.Config
import coursier.Fetch
import coursier.cache.Cache
import coursier.core._
import coursier.util.Task

import java.io.File
import java.nio.file.{FileSystems, Files, Path}
import scala.collection.mutable.ListBuffer

object BloopFiles {
  private val jsonPattern = FileSystems
    .getDefault
    .getPathMatcher("glob:**.json")

  def installSemanticdb(index: IndexCommand): List[Throwable] = {
    val errors = ListBuffer.empty[Throwable]
    val semanticdbJars: List[Path] =
      Fetch[Task](Cache.default)
        .addDependencies(
          Dependency(
            Module(
              Organization("com.sourcegraph"),
              ModuleName("semanticdb-javac"),
              Map.empty
            ),
            "0.1.0"
          )
        )
        .run()
        .map(_.toPath)
        .toList
    for {
      file <- index.workspace.resolve(".bloop").toFile.listFiles().iterator
      path = file.toPath
      if jsonPattern.matches(path) && Files.isRegularFile(path)
      config <-
        bloop.config.read(path) match {
          case Left(error) =>
            errors += error
            Nil
          case Right(value) =>
            value :: Nil
        }
    } {
      val compilerArgs: String = List(
        "-Xplugin:semanticdb",
        s"-targetroot:${index.targetrootAbsolutePath}",
        s"-sourceroot:${index.workspace}",
        index.verboseFlag,
        index.textFlag
      ).filter(_.nonEmpty).mkString(" ")
      val isProcessorpathEnabled = config
        .project
        .java
        .exists(_.options.contains("-processorpath"))
      val classpath =
        if (isProcessorpathEnabled)
          config.project.classpath
        else
          semanticdbJars ++ config.project.classpath

      val newJavaConfig = config
        .project
        .java
        .map { j =>
          val baseOptions =
            if (isProcessorpathEnabled) {
              patchOptions(j.options, semanticdbJars)
            } else {
              j.options
            }
          j.copy(options = compilerArgs :: baseOptions)
        }

      val newConfig = config.copy(project =
        config.project.copy(classpath = classpath, java = newJavaConfig)
      )
      bloop.config.write(newConfig, path)
    }
    errors.toList
  }

  private def patchOptions(
      options: List[String],
      extraJars: List[Path]
  ): List[String] =
    options match {
      case Nil =>
        Nil
      case "-processorpath" :: oldPath :: tail =>
        val classpath = ListBuffer.empty[String]
        classpath ++= extraJars.map(_.toString)
        classpath += oldPath
        val newPath = classpath.mkString(File.pathSeparator)
        "-processorpath" :: newPath :: patchOptions(tail, extraJars)
      case head :: tail =>
        if (head.startsWith("-Xplugin:semanticdb"))
          patchOptions(tail, extraJars)
        else
          head :: patchOptions(tail, extraJars)
    }

}
