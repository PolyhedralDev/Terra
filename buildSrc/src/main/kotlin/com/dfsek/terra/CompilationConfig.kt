package com.dfsek.terra

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.kotlin.dsl.*
import org.gradle.language.jvm.tasks.ProcessResources

fun Project.configureCompilation() {
    apply(plugin = "maven-publish")
    apply(plugin = "idea")

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_14
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        doFirst {
            options.compilerArgs.addAll(arrayOf("-Xlint:all", "--release", "8", "-Xplugin:jabel"))
        }
    }

    tasks.withType<ProcessResources> {
        include("**/*.*")
        filter<org.apache.tools.ant.filters.ReplaceTokens>(
                "tokens" to mapOf(
                        "VERSION" to project.version.toString(),
                        "DESCRIPTION" to project.properties["terra.description"],
                        "WIKI" to project.properties["terra.wiki"],
                        "SOURCE" to project.properties["terra.source"],
                        "ISSUES" to project.properties["terra.issues"],
                        "LICENSE" to project.properties["terra.license"]
                )
        )
    }

    tasks.withType<Javadoc> {
        options.encoding = "UTF-8"
    }

    tasks.withType<Jar> {
        archiveBaseName.set("Terra-${archiveBaseName.get()}")
        from("../LICENSE", "../../LICENSE")
    }

    tasks.register<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
    }

    tasks.register<Jar>("javadocJar") {
        dependsOn("javadoc")
        archiveClassifier.set("javadoc")
        from(tasks.getByName<Javadoc>("javadoc").destinationDir)
    }
}