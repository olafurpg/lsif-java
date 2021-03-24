package com.sourcegraph.package_server

final case class PostgresOptions(
    url: String = "localhost:5432/lsif-java-server",
    user: String = "postgres",
    password: String = "postgres"
)

object PostgresOptions {
  implicit val codec = moped.macros.deriveCodec(PostgresOptions())
}
