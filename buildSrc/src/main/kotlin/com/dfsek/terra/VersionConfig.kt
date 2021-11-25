package com.dfsek.terra

import org.gradle.api.Project

fun Project.versionProjects(project:String, version:String) {
    project(project).subprojects.forEach {
        it.version = version
    }
}

fun Project.version(version: String, preRelease: Boolean = false): String {
    return if (!preRelease)
        version
    else //Only use git hash if it's a prerelease.
        "$version-BETA+${getGitHash()}"
}
