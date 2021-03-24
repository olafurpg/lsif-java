package com.sourcegraph.package_server

import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque

import scala.jdk.CollectionConverters._

import moped.json.DecodingContext
import moped.json.JsonCodec
import moped.json.JsonElement
import moped.json.JsonNull
import moped.json.Result
import moped.macros.ClassShape
import moped.reporters.Diagnostic

case class Package(dependency: String, repositories: List[String])

trait PackageStore {
  def addPackage(pkg: Package): Unit
  def allPackages(): List[Package]

  def addIndexedPackage(pkg: String): Unit
  def allIndexedPackages(): Set[String]
}

object PackageStore {
  implicit val codec =
    new JsonCodec[PackageStore] {
      def decode(context: DecodingContext): Result[PackageStore] =
        Result.error(Diagnostic.error("Decoding PackageStore is not supported"))
      def encode(value: PackageStore): JsonElement = JsonNull()
      def shape: ClassShape = ClassShape.empty
    }
}

class InMemoryPackageStore extends PackageStore {
  val packages = new ConcurrentLinkedDeque[Package]
  val indexedPackages = Collections
    .newSetFromMap(new ConcurrentHashMap[String, java.lang.Boolean])

  override def addPackage(pkg: Package): Unit = packages.add(pkg)
  override def allPackages(): List[Package] = packages.asScala.toList
  override def addIndexedPackage(pkg: String): Unit = indexedPackages.add(pkg)
  override def allIndexedPackages(): Set[String] = indexedPackages.asScala.toSet

}

//   override def addDependency(dep: String, repositories: List[String]): Unit =
//     ???

// }
