package com.dfsek.terra

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.repositories

fun Project.configureDependencies() {
    apply(plugin = "java")
    apply(plugin = "java-library")

    configurations {
        val shaded = create("shaded")
        val shadedApi = create("shadedApi")
        shaded.extendsFrom(shadedApi)
        getByName("api").extendsFrom(shadedApi)
        val shadedImplementation = create("shadedImplementation")
        shaded.extendsFrom(shadedImplementation)
        getByName("implementation").extendsFrom(shadedImplementation)
    }

    repositories {
        maven { url = uri("https://maven.enginehub.org/repo/") }
        maven { url = uri("https://repo.codemc.org/repository/maven-public") }
        maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
        maven { url = uri("https://maven.fabricmc.net/") }
        gradlePluginPortal()
        jcenter()
        mavenCentral()
    }

    dependencies {
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.7.0")
        "testImplementation"("org.junit.jupiter:junit-jupiter-engine:5.7.0")
        "api"("org.jetbrains:annotations:20.1.0")
    }
}