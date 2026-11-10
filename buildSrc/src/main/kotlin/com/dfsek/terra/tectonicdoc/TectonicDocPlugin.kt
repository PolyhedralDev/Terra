package com.dfsek.terra.tectonicdoc

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension

class TectonicDocPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("generateDocs", GenerateDocsTask::class.java) {
            project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.configureEach {
                classDirectories.add(java.classesDirectory)
            }
            docsDirectory.set(project.layout.buildDirectory.dir("tectonic"))
        }
    }
}
