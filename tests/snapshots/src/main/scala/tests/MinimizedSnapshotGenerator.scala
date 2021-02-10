package tests

import scala.meta.io.AbsolutePath

class MinimizedSnapshotGenerator extends SnapshotGenerator {
  override def run(context: SnapshotContext, handler: SnapshotHandler): Unit = {
    val sourceroot = AbsolutePath(BuildInfo.sourceroot)
    val targetroot = AbsolutePath(BuildInfo.minimizedTargetroot)
    val sourceDirectory = AbsolutePath(BuildInfo.minimizedSourceDirectory)
    val files = SemanticdbFile.fromDirectory(
      AbsolutePath(BuildInfo.minimizedSourceDirectory),
      sourceroot,
      targetroot
    )
    files.foreach { file =>
      val relativeToSourceDirectory = file.javaPath.toRelative(sourceDirectory)
      val expectOutput = context
        .expectDirectory
        .resolve(relativeToSourceDirectory.toNIO)
      handler.onSnapshotTest(
        context,
        expectOutput,
        () => SemanticdbPrinters.printTextDocument(file.textDocument)
      )
    }
  }
}
