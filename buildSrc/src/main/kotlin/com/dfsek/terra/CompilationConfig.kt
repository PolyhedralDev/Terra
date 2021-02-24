package com.dfsek.terra

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.filter
import org.gradle.kotlin.dsl.withType
import org.gradle.language.jvm.tasks.ProcessResources

fun Project.configureCompilation() {
    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        doFirst {
            options.compilerArgs = mutableListOf("-Xlint:all")
        }
    }

    tasks.withType<ProcessResources> {
        include("**/*.*")
        filter<org.apache.tools.ant.filters.ReplaceTokens>(
                "tokens" to mapOf(
                        "VERSION" to project.version.toString()
                )
        )
    }

    tasks.withType<Javadoc> {
        options.encoding = "UTF-8"
    }
}