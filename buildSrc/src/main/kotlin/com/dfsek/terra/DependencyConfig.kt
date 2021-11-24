package com.dfsek.terra

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.repositories

fun Project.configureDependencies() {
    apply(plugin = "java")
    apply(plugin = "java-library")
    
    val api by configurations.getting
    val implementation by configurations.getting
    val testImplementation by configurations.getting
    val compileOnly by configurations.getting
    
    val shaded by configurations.creating
    val shadedApi by configurations.creating {
        shaded.extendsFrom(this)
        api.extendsFrom(this)
    }
    val shadedImplementation by configurations.creating {
        shaded.extendsFrom(this)
        implementation.extendsFrom(this)
    }
    
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.fabricmc.net/") }
        maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
        maven { url = uri("https://repo.codemc.org/repository/maven-public") }
        maven { url = uri("https://maven.enginehub.org/repo/") }
    }
    
    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")
        compileOnly("org.jetbrains:annotations:23.0.0")
    
        compileOnly("com.google.guava:guava:30.0-jre")
        testImplementation("com.google.guava:guava:30.0-jre")
    }
    
    if (project(":common:addons").subprojects.contains(this)) { // If this is an addon project, depend on the API.
        dependencies {
            compileOnly(project(":common:api"))
            testImplementation(project(":common:api"))
        }
    }
}