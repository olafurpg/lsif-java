package tests

import java.nio.file.Files

import scala.meta.internal.io.FileIO
import scala.meta.internal.semanticdb.Semanticdb.TextDocument
import scala.meta.internal.semanticdb.Semanticdb.TextDocuments
import scala.meta.io.AbsolutePath
import scala.meta.io.RelativePath

case class SemanticdbFile(
    sourceroot: AbsolutePath,
    targetroot: AbsolutePath,
    relativePath: RelativePath
) {
  def javaPath: AbsolutePath = sourceroot.resolve(relativePath)
  def semanticdbPath: AbsolutePath =
    targetroot
      .resolve("META-INF")
      .resolve("semanticdb")
      .resolve(relativePath.toString() + ".semanticdb")
  def textDocument: TextDocument = {
    val docs = TextDocuments.parseFrom(Files.readAllBytes(semanticdbPath.toNIO))
    if (docs.getDocumentsCount == 0)
      TextDocument.newBuilder().build()
    else
      docs.getDocuments(0)
  }
}

object SemanticdbFile {
  def fromDirectory(
      dir: AbsolutePath,
      sourceroot: AbsolutePath,
      targetroot: AbsolutePath
  ): Seq[SemanticdbFile] = {
    FileIO
      .listAllFilesRecursively(dir)
      .map { file =>
        SemanticdbFile(sourceroot, targetroot, file.toRelative(sourceroot))
      }
  }
}
