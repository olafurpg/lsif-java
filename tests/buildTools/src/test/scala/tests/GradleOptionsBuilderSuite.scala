package tests

import java.io.File

import scala.jdk.CollectionConverters._

import munit.FunSuite
import munit.TestOptions

class GradleOptionsBuilderSuite extends FunSuite {
  val targetroot = System.getProperty("semanticdb.targetroot")
  val sourceroot = System.getProperty("semanticdb.sourceroot")
  val processorpath = System.getProperty("semanticdb.processorpath")
  val Xplugin =
    s"-Xplugin:semanticdb -sourceroot:$sourceroot -targetroot:$targetroot"
  def check(
      options: TestOptions,
      original: List[String],
      expected: List[String]
  ): Unit = {
    test(options) {
      val obtained = new GradleOptionsBuilder(original).build().asScala.toList
      assertEquals(obtained, expected)
    }
  }

  check(
    "basic",
    List("-Arandom"),
    List("-Arandom", "-classpath", processorpath, Xplugin)
  )

  check(
    "only-classpath",
    List("-classpath", "a.jar"),
    List(
      "-classpath",
      List(processorpath, "a.jar").mkString(File.pathSeparator),
      Xplugin
    )
  )

  check(
    "only-processorpath",
    List("-processorpath", "a.jar"),
    List(
      "-processorpath",
      List(processorpath, "a.jar").mkString(File.pathSeparator),
      Xplugin
    )
  )

  check(
    "classpath-and-processorpath",
    List("-classpath", "a.jar", "-processorpath", "b.jar"),
    List(
      "-classpath",
      List(processorpath, "a.jar").mkString(File.pathSeparator),
      "-processorpath",
      List(processorpath, "b.jar").mkString(File.pathSeparator),
      Xplugin
    )
  )

}
