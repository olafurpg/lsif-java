package tests

class GradleBuildToolSuite extends BaseBuildToolSuite {

  checkBuild(
    "basic",
    """|/build.gradle
       |apply plugin: 'java'
       |/src/main/java/Example.java
       |public class Example {}
       |/src/test/java/ExampleSuite.java
       |public class ExampleSuite {}
       |""".stripMargin,
    2
  )

  checkBuild(
    "toolchains",
    """|/build.gradle
       |apply plugin: 'java'
       |java {
       |  toolchain {
       |    languageVersion = JavaLanguageVersion.of(8)
       |  }
       |}
       |/src/main/java/Example.java
       |public class Example {}
       |/src/test/java/ExampleSuite.java
       |public class ExampleSuite {}
       |""".stripMargin,
    2
  )
}
