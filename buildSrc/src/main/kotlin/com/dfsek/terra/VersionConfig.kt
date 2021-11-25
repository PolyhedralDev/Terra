package com.dfsek.terra

import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.Project
import org.gradle.kotlin.dsl.filter
import org.gradle.kotlin.dsl.withType
import org.gradle.language.jvm.tasks.ProcessResources

var isPrerelease = false

fun preRelease(preRelease: Boolean) {
    isPrerelease = preRelease
}

fun Project.versionProjects(project: String, version: String) {
    project(project).subprojects.forEach {
        it.version = version
    }
}

fun Project.version(version: String): String {
    return if (!isPrerelease)
        version
    else //Only use git hash if it's a prerelease.
        "$version-BETA+${getGitHash()}"
}

fun Project.configureVersioning() {
    tasks.withType<ProcessResources> {
        include("**/*.*")
        filter<ReplaceTokens>(
            "tokens" to mapOf(
                "VERSION" to version.toString()
                             )
                             )
    }
}