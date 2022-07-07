plugins {
    `kotlin-dsl`
    kotlin("jvm") version embeddedKotlinVersion
}

buildscript {
    configurations.all {
        resolutionStrategy {
            force("org.ow2.asm:asm:9.3") // TODO: remove when ShadowJar updates ASM version
            force("org.ow2.asm:asm-commons:9.3")
        }
    }
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
    implementation("gradle.plugin.com.github.jengelman.gradle.plugins:shadow:+")
    implementation("io.papermc.paperweight.userdev:io.papermc.paperweight.userdev.gradle.plugin:1.3.5")
    
    implementation("org.ow2.asm:asm:9.3")
    implementation("org.ow2.asm:asm-tree:9.3")
    implementation("com.dfsek.tectonic:common:4.2.0")
    implementation("org.yaml:snakeyaml:1.27")
}