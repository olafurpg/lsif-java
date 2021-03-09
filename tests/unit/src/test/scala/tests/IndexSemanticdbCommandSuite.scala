package tests

import com.sourcegraph.lsif_java.LsifJava
import moped.testkit.{FileLayout, MopedSuite}

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import scala.meta.internal.io.FileIO
import scala.meta.io.AbsolutePath

class IndexSemanticdbCommandSuite extends MopedSuite(LsifJava.app) {
  test("basic") {
    val targetroot = temporaryDirectory().resolve("targetroot")
    val output = temporaryDirectory().resolve("dump.lsif")
    val sourceroot = workingDirectory
    FileLayout.fromString(
      """|/example/Example1.java
         |package example;
         |
         |public class Example1 {
         |  public Example2 example;
         |}
         |/example/Example2.java
         |package example;
         |
         |public class Example2 {
         |  public Example1 example;
         |}
         |""".stripMargin,
      sourceroot
    )
    val compiler =
      new TestCompiler(TestCompiler.PROCESSOR_PATH, Nil, targetroot, sourceroot)
    val result = compiler.compileSemanticdbDirectory(workingDirectory)
    assert(result.isSuccess, clues(result))
    val exit = app().run(
      List("index-semanticdb", "--output", output.toString, targetroot.toString)
    )
    assert(exit == 0, clues(app.capturedOutput))
    val text = FileIO.slurp(AbsolutePath(output), StandardCharsets.UTF_8)
    pprint.log(text)
  }
}
