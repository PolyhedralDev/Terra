package com.dfsek.terra

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType

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
    val stdout = java.io.ByteArrayOutputStream()
    exec {
        commandLine = mutableListOf("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}