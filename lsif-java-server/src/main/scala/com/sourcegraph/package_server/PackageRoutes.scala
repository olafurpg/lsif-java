package com.sourcegraph.package_server

class PackageRoutes(server: PackageServer) extends cask.MainRoutes {
  override def host: String = server.host
  override def port: Int = server.port
  override def verbose: Boolean = server.verbose

  initialize()
}
