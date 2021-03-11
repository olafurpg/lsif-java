package tests

import com.jsoniter.any.Any
import com.jsoniter.output.JsonStream
import com.sourcegraph.lsif_protocol.{LsifCodegenConfig, LsifMetaData}
import com.sourcegraph.lsif_java.LsifJava
import moped.testkit.{FileLayout, MopedSuite}

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}
import java.util.Collections
import java.util.concurrent.atomic.AtomicReference
import scala.meta.internal.io.FileIO
import scala.meta.io.AbsolutePath
import scala.sys.process.Process

class IndexSemanticdbCommandSuite extends MopedSuite(LsifJava.app) {

  test("jsoniter".ignore) {
    new LsifCodegenConfig().setup()
    val project = new LsifMetaData()
    val json = JsonStream.serialize(project)
    pprint.log(json)
//    pprint.log(JsonStream.serialize(new User(42, "Susan")))
  }

  test("basic") {
    val targetroot = temporaryDirectory().resolve("targetroot")
    val output = temporaryDirectory().resolve("dump.lsif")
    val outputGolden = temporaryDirectory().resolve("dump-golden.lsif")
    val sourceroot = workingDirectory
    FileLayout.fromString(
      """|/example/Example1.java
         |package example;
         |
         | /** Example1 docstring */
         |public class Example1 {
         |  public Example2 example;
         |}
         |/example/Example2.java
         |package example;
         |
         | /** Example2 docstring */
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

    Process(
      List(
        "lsif-semanticdb",
        s"--semanticdbDir=$targetroot",
        s"--out=$outputGolden"
      )
    ).!
    printDump(output)
//    printDump(outputGolden)
    Process(List("lsif-validate", output.toString)).!
  }

  def printDump(path: Path): Unit = {
    val text = FileIO.slurp(AbsolutePath(path), StandardCharsets.UTF_8)
    val textWithLines = text
      .linesIterator
      .zipWithIndex
      .map { case (line, i) =>
        s"$i: $line"
      }
      .mkString("\n")
    pprint.log(textWithLines)
  }
}