import java.io.ByteArrayOutputStream
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.kotlin.dsl.support.serviceOf


var isPrerelease = false


fun Project.getGitHash(): String {
    return providers.exec {
        commandLine = mutableListOf("git", "rev-parse", "--short", "HEAD")
    }.standardOutput.asText.get().trim()
}

fun Project.gitClone(name: String) {
    providers.exec {
        commandLine = mutableListOf("git", "clone", name)
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