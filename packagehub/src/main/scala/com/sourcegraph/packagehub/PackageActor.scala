package com.sourcegraph.packagehub

import java.io.{ByteArrayOutputStream, File, IOException, PrintStream}
import java.nio.file.{
  FileSystems,
  FileVisitResult,
  Files,
  Path,
  Paths,
  SimpleFileVisitor,
  StandardCopyOption,
  StandardOpenOption
}
import java.nio.file.attribute.BasicFileAttributes
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._
import scala.meta.internal.io.FileIO
import scala.meta.io.AbsolutePath
import castor.Context
import castor.SimpleActor
import com.sourcegraph.io.DeleteVisitor
import com.sourcegraph.lsif_java.Dependencies
import com.sourcegraph.lsif_java.LsifJava
import coursier.core.Dependency
import moped.reporters.ConsoleReporter
import org.rauschig.jarchivelib.{
  ArchiveFormat,
  ArchiverFactory,
  CompressionType
}
import os.ProcessOutput.Readlines
import os.Shellable
import os.SubProcess
import ujson.Arr
import ujson.Bool
import ujson.Obj
import ujson.Str

import java.net.URL

/**
 * Actor that creates git repos from package sources and (optionally LSIF
 * indexes the sources.
 *
 * This class is implemented as an actor in order to support the use-case:
 * "schedule an LSIF index job for this package after 1 minute". We don't care
 * about the return value of that scheduled LSIF index job so the "fire and
 * forget" nature of actors is OK.
 */
class PackageActor(
    src: String,
    coursier: String,
    store: PackageStore,
    packagehubUrl: String,
    val dir: Path,
    val addr: Int = 3434
)(implicit ctx: Context, log: cask.Logger)
    extends SimpleActor[Package] {
  createGitConfig()

  def run(msg: Package): Unit = {
    lsifIndex(msg, lsifUpload = true)
  }
  private def tmpDir = dir.resolve("lsif-java-tmp")
  private var serveGit = Option.empty[SubProcess]
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
  def lsifIndex(msg: Package, lsifUpload: Boolean): Path = {
    commitSources(msg)
    val dump: Path = lsifIndexPackage(msg)
    if (lsifUpload && Files.isRegularFile(dump)) {
      os.proc(src, "login").call(stdout = os.Pipe, stderr = os.Pipe)
      os.proc(src, "lsif", "upload", "--no-progress", "--repo", msg.path)
        .call(
          stdout = os.Pipe,
          stderr = os.Pipe,
          cwd = os.Path(packageDir(msg))
        )
      store.addIndexedPackage(msg)
    }
    dump
  }

  def commitSources(pkg: Package): Unit = {
    if (isCached(pkg))
      return
    pkg match {
      case mvn @ MavenPackage(_) =>
        val deps = Dependencies
          .resolveDependencies(List(mvn.repr), transitive = false)
        indexDeps(mvn, deps)
      case JdkPackage(version) =>
        val home = os
          .proc(coursier, "java-home", "--jvm", version)
          .call()
          .out
          .trim()
        val srcs = List(
          Paths.get(home, "src.zip"),
          Paths.get(home, "lib", "src.zip")
        )
        srcs.find(Files.isRegularFile(_)) match {
          case Some(src) =>
            commitSourcesArtifact(pkg, src)
          case None =>
            throw new IllegalArgumentException(s"no such files: $srcs")
        }
      case npm: NpmPackage =>
        commitNpmPackage(npm)
    }
  }

  private def commitNpmPackage(npm: NpmPackage): Unit = {
    val repo = packageDir(npm)
    if (!isCached(npm)) {
      val tmp = Files.createTempDirectory("packagehub")
      val filename = tmp.resolve(npm.tarballFilename)
      val url = os
        .proc("npm", "info", npm.npmName, "dist.tarball")
        .call()
        .out
        .trim()
      val in = new URL(url).openStream()
      try Files.copy(in, filename, StandardCopyOption.REPLACE_EXISTING)
      finally in.close()
      ArchiverFactory
        .createArchiver(new File("archive.tgz"))
        .extract(filename.toFile, tmp.toFile)
      val allFiles = FileIO.listAllFilesRecursively(AbsolutePath(tmp))
      val packageJson = allFiles
        .find(_.toNIO.endsWith("package.json"))
        .getOrElse(
          sys.error(s"no such file: package.json (${allFiles.mkString(", ")})")
        )
      Files.createDirectories(repo.getParent)
      val parent = Paths.get(
        packageJson
          .toNIO
          .getParent
          .toRealPath()
          .toString
          .stripPrefix("/private")
      )
      moveDirectory(parent, repo)
      cacheDirectory(npm)
      Files.walkFileTree(tmp, new DeleteVisitor())
      gitInit(repo)
      gitCommitAll(npm.version, repo)
    }
  }
  private def moveDirectory(from: Path, to: Path): Unit = {
    Files.walkFileTree(
      from,
      new SimpleFileVisitor[Path] {
        override def visitFile(
            file: Path,
            attrs: BasicFileAttributes
        ): FileVisitResult = {
          val relpath = from.relativize(file)
          Files.copy(
            file,
            to.resolve(relpath),
            StandardCopyOption.REPLACE_EXISTING
          )
          FileVisitResult.CONTINUE

        }
        override def preVisitDirectory(
            dir: Path,
            attrs: BasicFileAttributes
        ): FileVisitResult = {
          val relpath = from.relativize(dir)
          val out = to.resolve(relpath)
          if (!Files.isDirectory(out))
            Files.createDirectory(out)
          FileVisitResult.CONTINUE
        }
      }
    )
  }
  private def indexDeps(dep: MavenPackage, deps: Dependencies): Unit = {
    deps
      .sourcesResult
      .fullDetailedArtifacts
      .foreach {
        case (dep, _, _, Some(file)) =>
          commitSourcesArtifact(MavenPackage(dep), file.toPath)
        case _ =>
      }
  }

  private def packageId(dep: Dependency): String = {
    s"${dep.module.organization.value}:${dep.module.name.value}:${dep.version}"
  }
  private def packageRelDir(dep: Dependency): Path = {
    Paths.get(dep.module.organization.value, dep.module.name.value, dep.version)
  }
  def packageTmpDir(dep: Dependency): Path = {
    tmpDir.resolve(packageRelDir(dep))
  }
  def packageDir(dep: Dependency): Path = {
    Paths.get(dep.module.organization.value, dep.module.name.value, dep.version)
  }
  def packageDir(pkg: Package): Path = {
    dir.resolve(pkg.relativePath)
  }
  private def collectAllJavaFiles(dir: Path): List[Path] = {
    val javaPattern = FileSystems.getDefault.getPathMatcher("glob:**.java")
    val buf = ListBuffer.empty[Path]
    Files.walkFileTree(
      dir,
      new SimpleFileVisitor[Path] {
        override def visitFile(
            file: Path,
            attrs: BasicFileAttributes
        ): FileVisitResult = {
          if (javaPattern.matches(file)) {
            buf += file
          }
          FileVisitResult.CONTINUE
        }
        override def visitFileFailed(
            file: Path,
            exc: IOException
        ): FileVisitResult = FileVisitResult.CONTINUE
      }
    )
    buf.toList
  }

  def lsifIndexPackage(pkg: Package): Path = {
    val sourceroot = packageDir(pkg)
    val dump = sourceroot.resolve("dump.lsif")
    if (!Files.isDirectory(sourceroot))
      return dump
    val out = new ByteArrayOutputStream
    val ps =
      new PrintStream(out) {
        override def toString(): String = "foobar"
      }
    val pipe = Readlines(line => ps.println(line))

    val index: Int =
      pkg match {
        case _: NpmPackage =>
          os.proc("npm", "install").call(cwd = os.Path(sourceroot))
          val tsconfig = sourceroot.resolve("tsconfig.json")
          if (!Files.isRegularFile(tsconfig)) {
            val config = Obj(
              "include" -> Arr("**/*"),
              "compilerOptions" -> Obj("allowJs" -> true)
            )
            Files.write(tsconfig, List(ujson.write(config, indent = 2)).asJava)
          }
          os.proc("npx", "@olafurpg/lsif-tsc", "-p", sourceroot.toString)
            .call(
              check = false,
              stdout = pipe,
              stderr = pipe,
              cwd = os.Path(sourceroot)
            )
            .exitCode
        case _ =>
          val env = LsifJava
            .app
            .env
            .withStandardOutput(ps)
            .withStandardError(ps)
          val app = LsifJava.app.withEnv(env).withReporter(ConsoleReporter(ps))
          app.run(
            List(
              "index",
              "--cwd",
              sourceroot.toString(),
              "--output",
              dump.toString(),
              "--build-tool",
              "lsif"
            )
          )
      }
    if (index != 0) {
      ps.flush()
      sys.error(out.toString())
    }
    dump
  }

  private def commitSourcesArtifact(dep: Package, file: Path): Unit = {
    val repo = packageDir(dep)
    Files.createDirectories(repo)
    gitInit(repo)
    deleteAllNonGitFiles(repo)
    FileIO.withJarFileSystem(AbsolutePath(file), create = false, close = true) {
      root =>
        FileIO
          .listAllFilesRecursively(root)
          .foreach { file =>
            val bytes = Files.readAllBytes(file.toNIO)
            val rel = file.toRelative(root).toURI(false).toString()
            val out = repo.resolve(rel)
            Files.createDirectories(out.getParent())
            Files.write(
              out,
              bytes,
              StandardOpenOption.CREATE,
              StandardOpenOption.TRUNCATE_EXISTING
            )
          }
        Files.walkFileTree(
          root.toNIO,
          new SimpleFileVisitor[Path] {
            override def visitFile(
                file: Path,
                attrs: BasicFileAttributes
            ): FileVisitResult = {
              FileVisitResult.CONTINUE
            }
          }
        )
    }
    val gitignore = ListBuffer[String]("dump.lsif", packagehubCached)
    dep match {
      case _: NpmPackage =>
        gitignore += "/tsconfig.json"
        gitignore += "node_modules/"
      case _ =>
    }
    Files.write(repo.resolve(".gitignore"), gitignore.asJava)
    val build = Obj()
    dep match {
      case MavenPackage(dep) =>
        build("dependencies") = Arr(packageId(dep))
      case JdkPackage(version) =>
        build("indexJdk") = Bool(false)
        build("jvm") = Str(version)
      case _ =>
    }
    Files.write(
      repo.resolve("lsif-java.json"),
      List(ujson.write(build, indent = 2)).asJava
    )
    cacheDirectory(dep)
    gitCommitAll(dep.version, repo)
  }

  val date = "Thu Apr 8 14:24:52 2021 +0200"
  def proc(repo: Path, command: String*) = {
    os.proc(Shellable(command))
      .call(cwd = os.Path(repo), env = Map("GIT_COMMITTER_DATE" -> date))
  }

  private def gitCommitAll(version: String, repo: Path): Unit = {
    val message = s"Version ${version}"
    proc(repo, "git", "add", ".")
    proc(repo, "git", "commit", "--allow-empty", "--date", date, "-m", message)
    proc(repo, "git", "tag", "-f", "-m", message, s"v${version}")
  }

  private def gitInit(repo: Path): Unit = {
    proc(repo, "git", "init")
  }
  private def deleteAllNonGitFiles(repo: Path): Unit = {
    val gitDir = repo.resolve(".git")
    val deleteNonGitFiles =
      new DeleteVisitor(deleteFile = file => !file.startsWith(gitDir))
    Files.walkFileTree(repo, deleteNonGitFiles)
  }

  private val packagehubCached = "packagehub_cached"

  private def cacheDirectory(pkg: Package): Unit = {
    Files
      .write(packageDir(pkg).resolve(packagehubCached), List[String]().asJava)
  }
  private def isCached(pkg: Package): Boolean = {
    Files.exists(packageDir(pkg).resolve(packagehubCached))
  }

}
