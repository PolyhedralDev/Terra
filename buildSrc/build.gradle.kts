plugins {
    `kotlin-dsl`
    kotlin("jvm") version embeddedKotlinVersion
}

repositories {
    // TODO: remove once Tectonic 4.4.0 is published
    mavenLocal {
        content {
            includeGroup("com.dfsek.tectonic")
        }
    }
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.solo-studios.ca/releases") {
        name = "Solo Studios"
    }
    maven("https://maven.solo-studios.ca/snapshots") {
        name = "Solo Studios"
    }
    maven("https://repo.codemc.org/repository/maven-public") {
        name = "CodeMC"
    }
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "PaperMC"
    }
}

dependencies {
    //TODO Allow pulling from Versions.kt
    implementation("com.gradleup.shadow:shadow-gradle-plugin:9.6.1")

    implementation("org.ow2.asm:asm:9.11.1")
    implementation("org.ow2.asm:asm-tree:9.11.1")
    implementation("com.dfsek.tectonic:common:4.4.0")
    implementation("org.yaml:snakeyaml:2.7")
}