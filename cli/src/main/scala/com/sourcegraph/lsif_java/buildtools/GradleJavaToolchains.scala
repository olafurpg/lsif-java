package com.sourcegraph.lsif_java.buildtools

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

import scala.jdk.CollectionConverters._

import com.sourcegraph.lsif_java.Embedded
import com.sourcegraph.lsif_java.IndexCommand

case class GradleJavaToolchains(
    toolchains: List[GradleJavaCompiler],
    index: IndexCommand,
    tmp: Path
) {

  def isEmpty: Boolean = toolchains.isEmpty

  def executableJavacPath(): Option[Path] = {
    if (toolchains.isEmpty) {
      Some(
        Embedded
          .customJavac(index.sourceroot, index.targetrootAbsolutePath, tmp)
      )
    } else {
      None
    }
  }

  def paths(): String = {
    val processorPath = Embedded.semanticdbJar(tmp)
    toolchains
      .map(_.createForwardingToolchain(index, tmp, processorPath))
      .mkString(",")
  }

}

object GradleJavaToolchains {

  /**
   * Extracts the "Java toolchains" that are used in this Gradle workspace.
   *
   * @see
   *   https://docs.gradle.org/6.7/userguide/toolchains.html
   */
  def fromWorkspace(
      index: IndexCommand,
      gradleCommand: String,
      tmp: Path
  ): GradleJavaToolchains = {
    val scriptPath = tmp.resolve("java-toolchains.gradle")
    val toolchainsPath = tmp.resolve("java-toolchains.txt")
    val taskName = "lsifDetectJavaToolchains"
    val script =
      s"""|allprojects {
          |  task $taskName {
          |    def out = java.nio.file.Paths.get('$toolchainsPath')
          |    doLast {
          |      tasks.withType(JavaCompile) {
          |        try {
          |          def lines = new ArrayList<String>()
          |          def path = javaCompiler.get().getExecutablePath()
          |          def version = javaCompiler.get().getMetadata().getLanguageVersion().asInt()
          |          def line = "$$version $$path"
          |          java.nio.file.Files.write(
          |            out,
          |            [line],
          |            java.nio.file.StandardOpenOption.APPEND,
          |            java.nio.file.StandardOpenOption.CREATE)
          |        } catch (Exception e) {
          |          // Ignore errors.
          |        }
          |      }
          |    }
          |  }
          |}
          |""".stripMargin
    Files.write(scriptPath, script.getBytes(StandardCharsets.UTF_8))
    index.process(gradleCommand, "--init-script", scriptPath.toString, taskName)
    val toolchains: List[GradleJavaCompiler] =
      if (Files.isRegularFile(toolchainsPath)) {
        Files
          .readAllLines(toolchainsPath)
          .asScala
          .flatMap(GradleJavaCompiler.fromLine)
          .toList
          .distinct
      } else {
        Nil
      }
    GradleJavaToolchains(toolchains, index, tmp)
  }
}
