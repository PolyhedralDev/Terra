plugins {
    `kotlin-dsl`
    kotlin("jvm") version embeddedKotlinVersion
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("gradle.plugin.com.github.jengelman.gradle.plugins:shadow:+")
    implementation("org.yaml:snakeyaml:1.27")
}