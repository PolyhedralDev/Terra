package com.dfsek.terra

import org.gradle.api.Project

var isPrerelease = false

fun preRelease(preRelease: Boolean) {
    isPrerelease = preRelease
}

fun Project.versionProjects(project:String, version:String) {
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
