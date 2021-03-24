package com.sourcegraph.package_server

import com.sourcegraph.lsif_java.BuildInfo
import moped.cli.Application
import moped.cli.Command
import moped.cli.CommandParser
import moped.commands.HelpCommand
import moped.commands.VersionCommand
import ujson.Arr
import ujson.Obj

case class PackageServer(
    store: PackageStore = new InMemoryPackageStore,
    port: Int = 8080,
    host: String = "localhost",
    verbose: Boolean = false
) extends Command {
  def run(): Int = {
    new PackageRoutes(this).main(Array())
    Thread.sleep(10000)
    0
  }

  @cask.get("/packages")
  def packages(): ujson.Value = Arr.from(store.allPackages().map(_.dependency))

  @cask.post("/packages")
  def addPackage(pkg: String, repositories: List[String]): ujson.Value = {
    store.addPackage(Package(pkg, repositories))
    store.addIndexedPackage(pkg)
    Obj()
  }

  @cask.get("/indexed-packages")
  def indexedPackages(): ujson.Value = Arr.from(store.allIndexedPackages())

}

object PackageServer {
  implicit val parser = CommandParser.derive(PackageServer())
  val app = Application
    .fromName(
      "lsif-java-server",
      BuildInfo.version,
      commands = List(
        CommandParser[HelpCommand],
        CommandParser[VersionCommand],
        CommandParser[PackageServer]
      )
    )
    .withIsSingleCommand(true)
  def main(args: Array[String]): Unit = {
    app.runAndExitIfNonZero(args.toList)
  }
}
