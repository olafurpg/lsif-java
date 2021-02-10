package tests

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

import munit.FailException
import munit.FunSuite
import munit.Location

trait AssertSnapshotHandlers {
  self: FunSuite =>

  class AssertSnapshotHandler extends SnapshotHandler {
    override def onSnapshotTest(
        context: SnapshotContext,
        expectFile: Path,
        obtainedOutput: () => String
    ): Unit = {
      val relativePath = context.expectDirectory.relativize(expectFile)
      self.test(relativePath.toString) {
        if (Files.isRegularFile(expectFile)) {
          val expected =
            new String(Files.readAllBytes(expectFile), StandardCharsets.UTF_8)
          val location = new Location(expectFile.toString, line = 0)
          self.assertNoDiff(obtainedOutput(), expected)(location)
        } else {
          throw new FailException(
            s"no snapshot file. To fix this problem, execute the command 'sbt snapshots/run'",
            cause = null,
            isStackTracesEnabled = false,
            location = Location.empty
          )
        }
      }
    }
  }
}
