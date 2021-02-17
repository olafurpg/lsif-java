package com.sourcegraph.lsif_java

import coursier.Fetch
import coursier.cache.Cache
import coursier.core.{Dependency, Module, ModuleName, Organization}
import coursier.parse.DependencyParser
import coursier.util.Task

import java.nio.file.Path

object Jars {
  def fetch(dep: String): List[Path] = {
    val Right(parsed) = DependencyParser
      .dependency(dep, defaultScalaVersion = BuildInfo.scalaVersion)
    Fetch[Task](Cache.default)
      .addDependencies(parsed)
      .run()
      .map(_.toPath)
      .toList
  }

}
