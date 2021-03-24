package com.sourcegraph.lsif_semanticdb

import java.io.PrintWriter
import java.nio.file.NoSuchFileException

import moped.cli.Application
import moped.progressbars.InteractiveProgressBar
import moped.reporters.Diagnostic

/**
 * Console reporter for index-semanticdb command.
 */
class ConsoleLsifSemanticdbReporter(app: Application)
    extends LsifSemanticdbReporter {

  val renderer = new LsifSemanticdbProgressRenderer
  val progressbar =
    new InteractiveProgressBar(
      new PrintWriter(app.env.standardError),
      renderer,
      isDynamicPartEnabled = app.env.isProgressBarEnabled
    )
  override def error(e: Throwable): Unit = {
    e match {
      case _: NoSuchFileException =>
        app.reporter.error(s"no such file: ${e.getMessage}")
      case _ =>
        app.reporter.log(Diagnostic.exception(e))
    }
  }

  override def hasErrors: Boolean = app.reporter.hasErrors()
  override def startProcessing(taskSize: Int): Unit = {
    renderer.totalSize = taskSize
    progressbar.start()
  }
  override def processedOneItem(): Unit = {
    renderer.currentSize.incrementAndGet()
  }
  override def endProcessing(): Unit = {
    progressbar.stop()
  }

}
