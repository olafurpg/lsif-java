package com.sourcegraph.package_server

import scala.util.control.NonFatal

import coursier.core.Dependency
import ujson.Obj
import coursier.core.Module
import coursier.core.Organization
import coursier.core.ModuleName

sealed abstract class Package(val id: String, val name: String) {
  def toJsonRepo: Obj = Obj("Name" -> name, "URI" -> s"/repos/$name")
}
object Package {
  def maven(org: String, name: String, version: String): MavenPackage = {
    MavenPackage(
      Dependency(
        Module(Organization(org), ModuleName(name), Map.empty),
        version
      )
    )
  }
  def fromString(value: String): Either[String, Package] = {
    value match {
      case s"maven:$library" =>
        Dependencies
          .parseDependencyEither(library)
          .flatMap { dep =>
            try {
              // Report an error if the dependency can't be resolved.
              Dependencies.resolveProvidedDeps(dep, Nil)
              Right(MavenPackage(dep))
            } catch {
              case NonFatal(e) =>
                Left(e.getMessage())
            }
          }
      case other =>
        Left(
          s"unsupported package '$other'. To fix this problem, use a valid syntax " +
            s"such as 'maven:ORGANIZATION:ARTIFACT_NAME_VERSION' for Java libraries."
        )
    }
  }
}
case class MavenPackage(dep: Dependency)
    extends Package(
      s"maven:${dep.module.repr}:${dep.version}",
      s"maven/${dep.module.organization.value}/${dep.module.name.value}/${dep.version}"
    ) {
  def repr = id.stripPrefix("maven:")
}
