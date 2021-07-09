package com.dfsek.terra

import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.gradle.language.jvm.tasks.ProcessResources
import java.io.File


fun Project.runDir(dir: File) {
    tasks.withType<ProcessResources> {
        doFirst {

        }
    }
}

fun Project.configureAddons() {
    tasks.withType<ProcessResources> {
        project(":common:addons").subprojects.forEach {
            it.afterEvaluate {
                dependsOn(it.tasks.getByName("build")) // Depend on addon JARs
            }
        }
    }
}