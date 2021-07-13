import com.dfsek.terra.configureCompilation
import com.dfsek.terra.configureDependencies

plugins {
    `java-library`
    `maven-publish`
    idea
}

configureCompilation()
configureDependencies()

group = "com.dfsek.terra.common"

dependencies {
    "shadedApi"(project(":common:api"))

    "shadedApi"("org.apache.commons:commons-rng-core:1.3")
    "shadedApi"("commons-io:commons-io:2.4")

    "shadedApi"("com.dfsek:Paralithic:0.3.2")
    "shadedApi"("net.jafama:jafama:2.3.2")
    "shadedApi"("org.yaml:snakeyaml:1.27")
    "shadedApi"("org.ow2.asm:asm:9.0")
    "shadedApi"("commons-io:commons-io:2.6")

    "shadedApi"("com.googlecode.json-simple:json-simple:1.1.1")
    "shadedApi"("org.yaml:snakeyaml:1.27")

    "compileOnly"("com.google.guava:guava:30.0-jre")

    "testImplementation"("com.google.guava:guava:30.0-jre")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks["sourcesJar"])
            artifact(tasks["jar"])
        }
    }

    repositories {
        val mavenUrl = "https://repo.codemc.io/repository/maven-releases/"
        val mavenSnapshotUrl = "https://repo.codemc.io/repository/maven-snapshots/"

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