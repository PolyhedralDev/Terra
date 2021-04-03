import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
}

configureCommon()

group = "com.dfsek.terra"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io/") }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    "shadedApi"(project(":common"))
    "shadedImplementation"("com.github.Querz:NBT:5.2") // Standalone NBT API
    "shadedImplementation"("org.yaml:snakeyaml:1.27")
    "shadedImplementation"("com.googlecode.json-simple:json-simple:1.1.1")
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("net.querz", "com.dfsek.terra.libs.nbt")
}