import com.dfsek.terra.configureCommon

plugins {
    `java-library`
    `maven-publish`
}

configureCommon()

group = "com.dfsek.terra.common"

dependencies {
    "shadedApi"("org.apache.commons:commons-rng-core:1.3")
    "shadedApi"("commons-io:commons-io:2.4")

    "shadedApi"("com.dfsek:Paralithic:0.1.0+21c6ff6")
    "shadedApi"("com.dfsek:Tectonic:1.2.3")
    "shadedApi"("net.jafama:jafama:2.3.2")
    "shadedApi"("org.yaml:snakeyaml:1.27")
    "shadedApi"("org.ow2.asm:asm:9.0")

    "compileOnly"("com.googlecode.json-simple:json-simple:1.1")

    "shadedApi"("com.google.guava:guava:30.0-jre")
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