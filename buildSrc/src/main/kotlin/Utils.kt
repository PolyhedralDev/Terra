import java.io.ByteArrayOutputStream
<<<<<<< HEAD
import org.gradle.api.Action
import org.gradle.api.Project


var isPrerelease = false


fun Project.getGitHash(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = mutableListOf("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}

fun Project.gitClone(name: String) {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = mutableListOf("git", "clone", name)
        standardOutput = stdout
    }
}

fun Project.forSubProjects(project: String, action: Action<Project>) {
    project(project).subprojects.forEach {
        action.execute(it)
    }
}

fun Project.forImmediateSubProjects(project: String, action: Action<Project>) {
    project(project).childProjects.forEach {
        action.execute(it.value)
    }
=======
import java.io.File
import org.gradle.api.Action
import org.gradle.api.Project

var isPrerelease = false

/**
 * Defensive git hash:
 * - If building from a ZIP (no .git) => "nogit"
 * - If git isn't on PATH or fails => "nogit"
 * - If git returns non-zero exit => "nogit"
 */
fun Project.getGitHash(): String {
    if(!File(rootDir, ".git").exists()) return "nogit"

    val stdout = ByteArrayOutputStream()
    return try {
        val result = exec {
            workingDir = rootDir
            commandLine("git", "rev-parse", "--short", "HEAD")
            isIgnoreExitValue = true
            standardOutput = stdout
        }
        if(result.exitValue != 0) "nogit" else stdout.toString().trim().ifBlank { "nogit" }
    } catch(_: Throwable) {
        "nogit"
    }
}

/**
 * Apply an action to all direct subprojects of a project path.
 */
fun Project.forSubProjects(projectPath: String, action: Project.() -> Unit) {
    project(projectPath).subprojects.forEach { it.action() }
}

/**
 * Java Action overload.
 */
fun Project.forSubProjects(projectPath: String, action: Action<Project>) {
    project(projectPath).subprojects.forEach { action.execute(it) }


/**
 * Apply an action to all *immediate* child projects of a project path (not recursively).
 * Used by the root build script for grouping tasks without descending into deeper modules.
 */

/**
 * Java-style Action overload.
 */
}

/**
 * Apply an action to all *immediate* child projects of a project path.
 * This differs from [forSubProjects], which iterates all subprojects (recursive).
 */
fun Project.forImmediateSubProjects(projectPath: String, action: Project.() -> Unit) {
    project(projectPath).childProjects.values.forEach { it.action() }
}

/**
 * Java-style Action overload.
 */
fun Project.forImmediateSubProjects(projectPath: String, action: Action<Project>) {
    project(projectPath).childProjects.values.forEach { action.execute(it) }
>>>>>>> 86e1828d0 (Initial fork: Terra 7.0.3 (lootfix + 1.21.10 compat + Java 21-25))
}

fun preRelease(preRelease: Boolean) {
    isPrerelease = preRelease
}

<<<<<<< HEAD
fun Project.versionProjects(project: String, version: String) {
    forSubProjects(project) {
        this.version = version
        logger.info("Setting version of $path to $version")
    }
    project(project).version = version
    logger.info("Setting version of $project to $version")
}

fun Project.version(version: String): String {
    return if (!isPrerelease)
        version
    else //Only use git hash if it's a prerelease.
        "$version-BETA+${getGitHash()}"
}
=======
fun Project.versionProjects(projectPath: String, version: String) {
    forSubProjects(projectPath) {
        this.version = version
        logger.info("Setting version of $path to $version")
    }
    project(projectPath).version = version
    logger.info("Setting version of $projectPath to $version")
}

fun Project.version(version: String): String {
    return if(!isPrerelease) version else "$version-BETA+${getGitHash()}"
}
>>>>>>> 86e1828d0 (Initial fork: Terra 7.0.3 (lootfix + 1.21.10 compat + Java 21-25))
