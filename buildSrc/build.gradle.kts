plugins {
    `kotlin-dsl`
    kotlin("jvm") version embeddedKotlinVersion
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.codemc.org/repository/maven-public") {
        name = "CodeMC"
    }
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "PaperMC"
    }
}

dependencies {
    //TODO Allow pulling from Versions.kt
    implementation("com.github.johnrengelman", "shadow", "8.1.1")
    implementation("io.papermc.paperweight.userdev", "io.papermc.paperweight.userdev.gradle.plugin", "1.7.1")

    implementation("org.ow2.asm", "asm", "9.7")
    implementation("org.ow2.asm", "asm-tree", "9.7")
    implementation("com.dfsek.tectonic", "common", "4.2.1")
    implementation("org.yaml", "snakeyaml", "2.2")
}