package com.dfsek.terra.tectonicdoc

import org.gradle.api.Plugin
import org.gradle.api.Project

class TectonicDocPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.create("generateDocs", GenerateDocsTask::class.java)
    }
}