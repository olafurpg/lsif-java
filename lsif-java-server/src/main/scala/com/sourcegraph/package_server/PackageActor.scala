package com.sourcegraph.package_server

import castor.Context
import castor.SimpleActor
import scala.jdk.CollectionConverters._
import java.nio.file.Path
import coursier.core.Dependency
import java.io.File
import java.nio.file.FileSystem
import java.nio.file.Files
import java.util.HashMap
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.FileSystemAlreadyExistsException
import java.nio.file.SimpleFileVisitor
import java.nio.file.FileVisitResult
import java.nio.file.attribute.BasicFileAttributes
import com.sourcegraph.io.DeleteVisitor
import java.nio.file.StandardOpenOption
import os.Shellable
import os.SubProcess

class PackageActor(
    src: String,
    store: PackageStore,
    val dir: Path,
    val addr: Int = 3434
)(implicit ctx: Context, log: cask.Logger)
    extends SimpleActor[Package] {
  private var serveGit = Option.empty[SubProcess]
  createGitConfig()
  Runtime
    .getRuntime()
    .addShutdownHook(
      new Thread() {
        override def run(): Unit = {
          shutdownGitServer()
        }
      }
    )
  private def shutdownGitServer(): Unit = {
    serveGit.foreach(_.destroy())
    serveGit = None
  }
  def restartGitServer(): Unit = {
    serveGit = Some(
      os.proc(src, "serve-git", "--addr", s":$addr").spawn(cwd = os.Path(dir))
    )
  }
  private def createGitConfig(): Unit = {
    def setConfig(key: String, value: String): Unit = {
      val result = os.proc("git", "config", "--get", key).call(check = false)
      if (result.exitCode != 0) {
        os.proc("git", "config", "--global", key, value).call()
      }
    }
    setConfig("user.name", "lsif-java")
    setConfig("user.email", "lsif-java@sourcegraph.com")
  }
  override def run(msg: Package): Unit = {
    msg match {
      case mvn @ MavenPackage(dep) =>
        indexDeps(Dependencies.resolveDependencies(List(mvn.repr), Nil))
    }
    restartGitServer()
  }

  def indexDeps(deps: Dependencies): Path = {
    deps
      .sourcesResult
      .fullDetailedArtifacts
      .foreach {
        case (dep, _, _, Some(file)) =>
          val dependency = s"${dep.module.repr}:${dep.version}"
          log.debug(dependency)
          commitSourcesArtifact(dep, file)
          indexClasspathArtifact(dep, deps.classpath)
        case _ =>
      }
    dir
  }
  private def indexClasspathArtifact(
      dep: Dependency,
      classpath: Seq[Path]
  ): Unit = {}

  private def commitSourcesArtifact(dep: Dependency, file: File): Unit = {
    val repo = dir.resolve(dep.module.repr)
    def proc(command: String*) = {
      os.proc(Shellable(command)).call(cwd = os.Path(repo))
    }
    Files.createDirectories(repo)
    proc("git", "init")
    val gitDir = repo.resolve(".git")
    val deleteNonGitFiles =
      new DeleteVisitor(deleteFile = file => !file.startsWith(gitDir))
    Files.walkFileTree(repo, deleteNonGitFiles)
    withJarFileSystem(file.toPath(), create = false, close = true) { root =>
      Files.walkFileTree(
        root,
        new SimpleFileVisitor[Path] {
          override def visitFile(
              file: Path,
              attrs: BasicFileAttributes
          ): FileVisitResult = {
            val bytes = Files.readAllBytes(file)
            val rel = root.relativize(file).iterator().asScala.map(_.toString)
            val out =
              rel.foldLeft(repo) { case (parent, child) =>
                parent.resolve(child)
              }
            Files.createDirectories(out.getParent())
            Files.write(out, bytes, StandardOpenOption.CREATE)
            FileVisitResult.CONTINUE
          }
        }
      )
    }
    val message = s"Version ${dep.version}"
    proc("git", "add", ".")
    proc("git", "commit", "--allow-empty", "-m", message)
    proc("git", "tag", "-f", "-m", message, s"v${dep.version}")
  }

  private def withJarFileSystem[T](
      path: Path,
      create: Boolean,
      close: Boolean = false
  )(f: Path => T): T = {
    val fs = newJarFileSystem(path, create)
    val root = fs.getPath("/")
    if (create || close) {
      try f(root)
      finally fs.close()
    } else {
      // NOTE(olafur): We don't fs.close() because that can affect another place where `FileSystems.getFileSystems`
      // was used due to a `FileSystemAlreadyExistsException`. This leaks resources, but I see no alternative that does
      // not involve refactoring everything to java.io or global mutable state for reference counting open file systems
      // per zip file.
      f(root)
    }
  }

  private def newJarFileSystem(path: Path, create: Boolean): FileSystem = {
    if (create && !Files.exists(path.getParent)) {
      Files.createDirectories(path.getParent)
    }
    val map = new HashMap[String, String]()
    if (create) {
      map.put("create", "true")
    }
    val uri = URI.create("jar:" + path.toUri.toString)
    newFileSystem(uri, map)
  }
  private def newFileSystem(
      uri: URI,
      map: java.util.Map[String, _]
  ): FileSystem =
    try FileSystems.newFileSystem(uri, map)
    catch {
      case _: FileSystemAlreadyExistsException =>
        FileSystems.getFileSystem(uri)
    }
}
