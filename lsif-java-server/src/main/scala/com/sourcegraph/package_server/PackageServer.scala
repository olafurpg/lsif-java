package com.sourcegraph.package_server

import java.nio.file.Path

import scala.concurrent.Await
import scala.concurrent.Promise
import scala.concurrent.duration.Duration

import cask.main.Main
import com.sourcegraph.lsif_java.BuildInfo
import io.undertow.Undertow
import moped.cli.Application
import moped.cli.Command
import moped.cli.CommandParser
import moped.commands.HelpCommand
import moped.commands.VersionCommand
import castor.Context
import cask.util.Logger
import org.flywaydb.core.Flyway
import moped.annotations.Hidden

case class PackageServer(
    store: PackageStore = new InMemoryPackageStore,
    port: String = "8080",
    host: String = "localhost",
    verbose: Boolean = false,
    sourcegraphEndpoint: String = "https://sourcegraph.com",
    dir: Option[Path] = None,
    app: Application = Application.default,
    @Hidden() cancelToken: Promise[Unit] = Promise(),
    ctx: Context = castor.Context.Simple.global,
    postgres: Option[PostgresOptions] = None,
    src: String = "src"
) extends Command {
  val log = new Logger.Console()
  def run(): Int = {
    if (!verbose)
      Main.silenceJboss()
    val actor =
      new PackageActor(src, store, dir.getOrElse(app.env.dataDirectory))(
        ctx,
        log
      )
    val routes = new PackageRoutes(this, actor, log)
    val server = Undertow
      .builder
      .addHttpListener(routes.port, host)
      .setHandler(routes.defaultHandler)
      .build()
    server.start()
    app.info(s"Listening on http://$host:$port")
    try Await.result(cancelToken.future, Duration.Inf)
    finally server.stop()
    0
  }

  def runDatabaseMigrations(): Unit = {
    postgres.foreach { p =>
      val result = Flyway
        .configure()
        .dataSource(s"jdbc:postgresql://${p.url}", p.user, p.password)
        .load()
        .migrate()
      result
        .warnings
        .forEach { warning =>
          log.debug(warning)
        }
    }
  }

}

object PackageServer {
  implicit val promiseCodec = new EmptyJsonCodec[Promise[Unit]]
  implicit val contextCodec = new EmptyJsonCodec[Context]
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
