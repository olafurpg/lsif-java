package lsifjava

import org.gradle.tooling.GradleConnector
import org.gradle.tooling.model.eclipse.EclipseProject
import org.gradle.tooling.model.idea.IdeaProject
import org.gradle.tooling.model.idea.IdeaSingleEntryLibraryDependency
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.LazyThreadSafetyMode.NONE

interface BuildToolInterface {
    val classpaths: List<Classpath>
    val sourceDirectories: List<List<Path>>
    val javaSourceVersions: List<String?>
    val sourcesList: List<Path>
}

// TODO(nsc) exclusions? lazy eval?
class GradleInterface(private val projectDir: CanonicalPath): AutoCloseable, BuildToolInterface {
    private val initScriptName = "projectClasspathFinder.gradle"

    private val artifactPattern = "lsifjava (.+)(?:\r?\n)".toRegex()

    // TODO(nsc) this depends on fetching classpaths first
    override val sourcesList: MutableList<Path> = arrayListOf()

    private val projectConnection = GradleConnector.newConnector()
            .forProjectDirectory(projectDir.path.toFile())
            .connect()

    private val eclipseModel by lazy(NONE) { projectConnection.getModel(EclipseProject::class.java) }

    private val ideaModel by lazy(NONE) { projectConnection.getModel(IdeaProject::class.java) }

    private val initScript = File.createTempFile("lsifjava", ".gradle").apply {
        this.deleteOnExit()
        this.bufferedWriter().use { configWriter ->
            ClassLoader.getSystemResourceAsStream(initScriptName)!!.bufferedReader().use { configReader ->
                configReader.copyTo(configWriter)
            }
        }
    }


    init {
        runCatching {
            // download sources on start
            projectConnection.newBuild()
                .addArguments("-I", initScript.absolutePath)
                .forTasks("idea")
                .run()
        }
    }

    // is this even *correct*? Is the order the same?
    override val classpaths: List<Classpath> get() {
        val initScriptClasspath = kotlin.runCatching { getClasspathFromInitScript() }.getOrDefault(Classpath(setOf()))

        val eclipseClasspaths = kotlin.runCatching { eclipseClasspath() }.getOrDefault(listOf())

        val ideaClasspath = kotlin.runCatching { ideaClasspath() }.getOrDefault(listOf())

        val merged = ideaClasspath.mapIndexed {i, it ->
            it + initScriptClasspath +
                eclipseClasspaths.getOrElse(i) { Classpath(setOf())}
        }

        merged.forEach { it ->
            it.forEach inner@ { path ->
                val sourceJar = Paths.get(path).toFile().parentFile.parentFile.walk().asSequence().firstOrNull {
                    it.isFile && it.name.endsWith("-sources.jar")
                } ?: return@inner
                sourcesList.add(Paths.get(sourceJar.canonicalPath))
            }
        }

        return merged
    }

    private fun ideaClasspath() = ideaModel.children.map { it ->
        Classpath(it.dependencies
            .filterIsInstance<IdeaSingleEntryLibraryDependency>()
            .map { it.file.canonicalPath }
            .toSet()
        )
    }

    private fun eclipseClasspath(project: EclipseProject = eclipseModel): List<Classpath> {
        val classPaths = arrayListOf<Classpath>()
        classPaths += Classpath(project.classpath.map { it.file.canonicalPath }.toSet())
        project.children.forEach { eclipseClasspath(it).toCollection(classPaths) }
        return classPaths
    }

    private fun getClasspathFromInitScript(): Classpath {
        // Unix only for now. To be revisited
        val (stdout, _) = execAndReadStdoutAndStderr("./gradlew -I ${initScript.absolutePath} lsifjavaAllGradleDeps", projectDir.path)

        val artifacts = artifactPattern.findAll(stdout)
            .mapNotNull { it.groups[1] }
            .map { Paths.get(it.value).toFile().canonicalPath }
            .toSet()
            
        return Classpath(artifacts)
    }

    override val sourceDirectories: List<List<Path>> get() {
        return ideaModel.children.flatMap { module ->
            module.contentRoots.map { root ->
                root.sourceDirectories.map { Paths.get(it.directory.canonicalPath) } +
                root.testDirectories.map { Paths.get(it.directory.canonicalPath) } +
                arrayListOf<Path>(
                    Paths.get(module.gradleProject.projectDirectory.path, "src/main"),
                    Paths.get(module.gradleProject.projectDirectory.path, "src/test")
                )
            }
        }
    }

    //override fun getSourceDirectories() = sourceDirectories(eclipseModel)

    private fun sourceDirectories(project: EclipseProject): List<List<Path>> {
        val sourceDirs = arrayListOf<List<Path>>()
        sourceDirs +=
            arrayListOf<Path>(
                Paths.get(project.projectDirectory.path, "src/main"),
                Paths.get(project.projectDirectory.path, "src/test")
            ) +
            project.sourceDirectories.map { Paths.get(project.projectDirectory.path, it.path) }
        project.children.forEach { sourceDirectories(it).toCollection(sourceDirs) }
        return sourceDirs
    }
    
    override val javaSourceVersions: List<String?> get() {
        return runCatching { javaSourceVersion(eclipseModel) }.getOrDefault(listOf())
    }

    // get rid of String?, use parent version or else fallback
    private fun javaSourceVersion(project: EclipseProject): List<String?> {
        val javaVersions = arrayListOf<String?>()
        javaVersions.add(project.javaSourceSettings?.sourceLanguageLevel?.toString())
        project.children.forEach { javaSourceVersion(it).toCollection(javaVersions) }
        return javaVersions
    }

    override fun close() = projectConnection.close()
}