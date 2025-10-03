plugins {
    `kotlin-dsl`
    kotlin("jvm") version embeddedKotlinVersion
}

repositories {
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
    implementation("com.gradleup.shadow", "shadow-gradle-plugin", "8.3.6")
    implementation("me.champeau.jmh", "me.champeau.jmh.gradle.plugin", "0.7.3")
    implementation("io.papermc.paperweight.userdev", "io.papermc.paperweight.userdev.gradle.plugin", "2.0.0-beta.17")
    implementation("org.ow2.asm", "asm", "9.8")
    implementation("org.ow2.asm", "asm-tree", "9.8")
    implementation("com.dfsek.tectonic", "common", "4.2.1")
    implementation("org.yaml", "snakeyaml", "2.4")
}