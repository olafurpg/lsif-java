package com.sourcegraph.package_server

import cask.model.Response
import cask.model.Status
import ujson.Arr
import ujson.Obj
import ujson.Value
import cask.util.Logger
import io.undertow.server.handlers.BlockingHandler
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import io.undertow.server.handlers.proxy.ProxyHandler
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient
import java.net.URI
import java.nio.file.Files
import scala.collection.mutable.ListBuffer
import scala.collection.mutable
import os.Shellable

class PackageRoutes(
    server: PackageServer,
    actor: PackageActor,
    override implicit val log: Logger
) extends cask.MainRoutes {
  def store = server.store
  override def host: String = server.host
  override def port: Int = server.port.toInt
  override def verbose: Boolean = server.verbose

  val srcProxy = ProxyHandler
    .builder()
    .setProxyClient(
      new LoadBalancingProxyClient()
        .addHost(URI.create(s"http://0.0.0.0:${actor.addr}"))
    )
    .build()
  override def defaultHandler: BlockingHandler = {
    val base = super.defaultHandler
    new BlockingHandler(
      new HttpHandler {
        def handleRequest(exchange: HttpServerExchange): Unit = {
          log.debug(exchange.getRequestPath())
          base.handleRequest(exchange)
        }
      }
    )
  }
  // override def defaultHandler: BlockingHandler = {
  //   val default = super.defaultHandler
  //   new BlockingHandler(
  //     new HttpHandler {
  //       def handleRequest(exchange: HttpServerExchange): Unit = {
  //         if (exchange.getRequestPath.startsWith("/lsif-java")) {
  //           default.handleRequest(exchange)
  //         } else {
  //           srcProxy.handleRequest(exchange)
  //         }
  //       }
  //     }
  //   )
  // }

  @cask.get("/v1/list-repos")
  def listRepos(): ujson.Value = {
    val items = Arr.from(store.allPackages().map(_.toJsonRepo))
    Obj("Items" -> items)
  }

  @cask.get("/repos", subpath = true)
  def repo(request: cask.Request): cask.Response[cask.Response.Data] = {
    request.remainingPathSegments.toList match {
      case "maven" :: org :: name :: version :: requestPath =>
        val path = actor.dir.resolve(request.exchange.getRequestPath())
        if (!Files.exists(path)) {
          val pkg = Package.maven(org, name, version)
          val deps = Dependencies.resolveDependencies(
            List(pkg.repr),
            repos = Nil,
            transitive = false
          )
          actor.indexDeps(deps)
        }
        val args = ListBuffer[String](
          "git",
          // Partial clones/fetches
          "-c",
          "uploadpack.allowFilter=true",
          // Can fetch any object. Used in case of race between a resolve ref and a
          // fetch of a commit. Safe to do, since this is only used internally.
          "-c",
          "uploadpack.allowAnySHA1InWant=true",
          "upload-pack",
          "--stateless-rpc"
        )
        val headers = mutable.Map.empty[String, String]
        requestPath match {
          case "info" :: "refs" :: Nil =>
            headers("Content-Type") =
              "application/x-git-upload-pack-advertisement"
            request
              .exchange
              .getOutputStream()
              .write(packetWrite("# service=git-upload-pack\n"))
            request.exchange.getOutputStream().write("0000".getBytes())
            args += "--advertise-refs"
          case "git-upload-pack" :: Nil =>
            headers("Content-Type") = "application/x-git-upload-pack-result"
          case _ =>
            cask.model.StaticFile(path.toString(), Nil)
        }
        args += actor.dir.toString()
        val result = os.proc(Shellable(args.toSeq)).call(check = false)
        request.exchange.getOutputStream.write(result.out.bytes)
        if (result.exitCode != 0) {
          badRequest(result.err.text())
        } else {
          ()
        }
      case _ =>
        badRequest(
          "invalid repo name (want /repos/maven/ORGANIZATION/NAME/VERSION/...)"
        )
    }
  }
  private def packetWrite(str: String): Array[Byte] = {
    var s = Integer.toString(str.length() + 4, 16)
    val modulo = s.length() % 4
    if (modulo != 0) {
      val padding = "0" * (4 - modulo)
      s = padding + s
    }
    (s + str).getBytes()
  }

  @cask.get("/lsif-java/packages")
  def packages(): ujson.Value = Arr.from(store.allPackages().map(_.id))

  @cask.post("/lsif-java/packages/:pkg")
  def addPackage(
      pkg: String,
      repositories: Seq[String] = Nil
  ): cask.Response[Value] = {
    Package.fromString(pkg) match {
      case Left(error) =>
        badRequest(error)
      case Right(p) =>
        store.addPackage(p)
        cask.Response(Obj())
    }
  }

  @cask.get("/lsif-java/indexed-packages")
  def indexedPackages(): ujson.Value = Arr.from(store.allIndexedPackages())

  private def badRequest(error: String): Response[Value] =
    errorResponse(error, Status.BadRequest.code)
  private def errorResponse(error: String, code: Int): Response[Value] =
    Response(Obj("error" -> error), code, Nil, Nil)

  initialize()
}
