plugins {
    `kotlin-dsl`
    kotlin("jvm") version embeddedKotlinVersion
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven { url = uri("https://repo.codemc.org/repository/maven-public") }
}

dependencies {
    implementation("gradle.plugin.com.github.jengelman.gradle.plugins:shadow:+")
    implementation("org.ow2.asm:asm:9.2")
    implementation("org.ow2.asm:asm-tree:9.2")
    implementation("com.dfsek.tectonic:common:4.2.0")
    implementation("org.yaml:snakeyaml:1.27")
}