package com.dfsek.terra

import java.io.ByteArrayOutputStream
import org.gradle.api.Project

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

fun Project.version(major: String, minor: String, revision: String, preRelease: Boolean = false): String {
    return if (!preRelease)
        "$major.$minor.$revision"
    else //Only use git hash if it's a prerelease.
        "$major.$minor.$revision-BETA+${getGitHash()}"
}

