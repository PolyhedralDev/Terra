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
    maven("https://papermc.io/repo/repository/maven-public/") {
        name = "PaperMC"
    }
}

dependencies {
    //TODO Allow pulling from Versions.kt
    implementation("com.github.johnrengelman", "shadow", "8.1.1")
    implementation("io.papermc.paperweight.userdev", "io.papermc.paperweight.userdev.gradle.plugin", "1.5.6")

    implementation("org.ow2.asm", "asm", "9.5")
    implementation("org.ow2.asm", "asm-tree", "9.5")
    implementation("com.dfsek.tectonic", "common", "4.2.0")
    implementation("org.yaml", "snakeyaml", "2.2")
}