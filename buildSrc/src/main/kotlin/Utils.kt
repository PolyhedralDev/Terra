import java.io.ByteArrayOutputStream
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
}

fun preRelease(preRelease: Boolean) {
    isPrerelease = preRelease
}

fun Project.versionProjects(project: String, version: String) {
    forSubProjects(project) {
        this.version = version
        println("Setting version of $path to $version")
    }
    project(project).version = version
    println("Setting version of $project to $version")
}

fun Project.version(version: String): String {
    return if (!isPrerelease)
        version
    else //Only use git hash if it's a prerelease.
        "$version-BETA+${getGitHash()}"
}