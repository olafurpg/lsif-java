package com.sourcegraph.lsif_java

import com.sourcegraph.lsif_semanticdb.LsifSemanticdbReporter
import moped.cli.Application
import moped.reporters.Diagnostic

import java.nio.file.Path

class MopedLsifReporter(app: Application) extends LsifSemanticdbReporter {
  override def error(e: Throwable): Unit =
    app.reporter.log(Diagnostic.exception(e))

  override def hasErrors: Boolean = app.reporter.hasErrors()
}
