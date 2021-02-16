package com.sourcegraph.lsif_java

import moped.cli.{Application, CommandParser}
import moped.commands.{HelpCommand, VersionCommand}

object LsifJava {
  val app: Application = Application.fromName(
    binaryName = "lsif-java",
    BuildInfo.version,
    List(
      CommandParser[HelpCommand],
      CommandParser[VersionCommand],
      CommandParser[IndexCommand]
    )
  )
  def main(args: Array[String]): Unit = {
    app.runAndExitIfNonZero(args.toList)
  }
}
