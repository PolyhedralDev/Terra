package com.dfsek.terra

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import java.io.ByteArrayOutputStream

fun Project.configureCommon() {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "idea")

    configureDependencies()
    configureCompilation()
    configureDistribution()

    version = rootProject.version



    tasks.withType<Test>().configureEach {
        useJUnitPlatform()

        maxHeapSize = "2G"
        ignoreFailures = false
        failFast = true
        maxParallelForks = 12
    }
}

fun Project.getGitHash(): String {
    val stdout = ByteArrayOutputStream()
    val result = exec {
        isIgnoreExitValue = true
        commandLine = mutableListOf("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    when (result.exitValue) {
        128 -> {
            project.logger.error("You must git clone the repository. You cannot build from a zip/tarball of the sources.")
            result.rethrowFailure().assertNormalExitValue()
        }
        127 -> {
            project.logger.error("Could not find git executable. Please install git. https://git-scm.com/downloads")
            result.rethrowFailure().assertNormalExitValue()
        }
        0 -> { // do nothing
        }
        else -> {
            project.logger.error("An error may or may not have occurred. The exit code was not zero.")
            result.rethrowFailure().assertNormalExitValue()
        }
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
