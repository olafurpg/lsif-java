import scala.util.Properties

def scala213 = "2.13.4"
def scalametaVersion = "4.4.8"
inThisBuild(
  List(
    scalaVersion := scala213,
    crossScalaVersions := List(scala213),
    organization := "com.sourcegraph",
    scalafixDependencies +=
      "com.github.liancheng" %% "organize-imports" % "0.5.0",
    scalacOptions ++= List("-Wunused:imports"),
    semanticdbEnabled := true,
    semanticdbVersion := scalametaVersion
  )
)

name := "root"
bloopGenerate.in(Compile) := None
bloopGenerate.in(Test) := None

lazy val testSettings = List(
  skip.in(publish) := true,
  autoScalaLibrary := true,
  testFrameworks := List(new TestFramework("munit.Framework")),
  libraryDependencies ++=
    List(
      "org.scalameta" %% "munit" % "0.7.10",
      "org.scalameta" %% "scalameta" % scalametaVersion,
      "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0",
      "io.get-coursier" %% "coursier" % "2.0.8",
      "com.lihaoyi" %% "pprint" % "0.6.1"
    )
)

lazy val plugin = project
  .in(file("semanticdb-javac"))
  .settings(
    moduleName := "semanticdb-javac",
    autoScalaLibrary := false,
    incOptions ~= { old =>
      old.withEnabled(false)
    },
    crossPaths := false,
    PB.targets.in(Compile) :=
      Seq(PB.gens.java -> (Compile / sourceManaged).value)
  )

lazy val minimized = project
  .in(file("tests/minimized"))
  .settings(
    autoScalaLibrary := false,
    javacOptions.in(Compile) ++=
      List[String](
        s"-Arandomtimestamp=${System.nanoTime()}",
        List(
          s"-Xplugin:semanticdb-javac",
          s"-text:on",
          s"-verbose",
          s"-sourceroot:${baseDirectory.in(ThisBuild).value}",
          s"-targetroot:${target.in(Compile).value / "semanticdb"}"
        ).mkString(" ")
      )
  )
  .dependsOn(plugin)

lazy val unit = project
  .in(file("tests/unit"))
  .settings(
    testSettings,
    buildInfoKeys :=
      Seq[BuildInfoKey](
        version,
        scalaVersion,
        "sourceroot" -> baseDirectory.in(ThisBuild).value,
        "minimizedSourceDirectory" ->
          sourceDirectory.in(minimized, Compile).value / "java",
        "minimizedTargetroot" ->
          target.in(minimized, Compile).value / "semanticdb"
      ),
    buildInfoPackage := "tests"
  )
  .dependsOn(plugin)
  .enablePlugins(BuildInfoPlugin)

lazy val snapshots = project
  .in(file("tests/snapshots"))
  .settings(
    testSettings,
    buildInfoKeys :=
      Seq[BuildInfoKey](
        "snapshotDirectory" -> sourceDirectory.in(Compile).value / "generated"
      ),
    buildInfoPackage := "tests.snapshots"
  )
  .dependsOn(minimized, unit)
  .enablePlugins(BuildInfoPlugin)
