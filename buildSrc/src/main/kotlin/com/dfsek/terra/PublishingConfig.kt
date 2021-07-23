package com.dfsek.terra

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.*

fun Project.configurePublishing() {
    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("mavenJava") {
                artifact(tasks["sourcesJar"])
                artifact(tasks["jar"])
            }
        }

        repositories {
            val mavenUrl = "https://repo.codemc.io/repository/maven-releases/"
            //val mavenSnapshotUrl = "https://repo.codemc.io/repository/maven-snapshots/"

            maven(mavenUrl) {
                val mavenUsername: String? by project
                val mavenPassword: String? by project
                if (mavenUsername != null && mavenPassword != null) {
                    credentials {
                        username = mavenUsername
                        password = mavenPassword
                    }
                }
            }
        }
    }
}