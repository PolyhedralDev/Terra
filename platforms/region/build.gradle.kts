@file:Suppress("UnstableApiUsage")

import com.dfsek.terra.configureCommon
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    application
}

configureCommon()

group = "com.dfsek.terra"
val regionVersion = "0.0.0"

application {
//    mainClassName = "com.dfsek.terra.RegionGenerator"
    mainClass.set("com.dfsek.terra.cmd.RegionCliInterface")
    executableDir = "./exec_dir"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
    val dir = file("testDir")
    dir.mkdirs()
    workingDir = dir
//    args = listOf("--carver-cache-size", "1024", "--max-recursions", "1024", "--seed", "0000", "--sampler-cache-size", "1024", "--structure-cache-size", "256")
    args = listOf("--debug", "circle", "r=64", "x=0", "z=0")
//    args = listOf("square", "s=128", "x=0", "z=0")
//    jvmArgs = listOf("-Djava.awt.headless=true")
}

tasks.named<JavaExec>("runShadow") {
    val dir = file("testDir")
    dir.mkdirs()
    workingDir = dir
    args = listOf("--help")
    jvmArgs = listOf("-Djava.awt.headless=true")

}

distributions {
    create("region") {
        @Suppress("DEPRECATION")
        baseName = "region"
        contents {
            from(tasks.named("shadowJar")) {
                into("lib")
            }
        }
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io/") }
}

dependencies {
    "shadedApi"(project(":common"))
    "shadedImplementation"("com.github.Querz:NBT:6.0") // Standalone NBT API

    "shadedImplementation"("org.yaml:snakeyaml:1.27")
    "shadedImplementation"("com.googlecode.json-simple:json-simple:1.1.1")
    "shadedImplementation"("commons-cli:commons-cli:1.4") // Used for command line argument parsing
    "shadedImplementation"("com.googlecode.lanterna:lanterna:3.1.1") // Used for the terminal UI
    "shadedImplementation"("info.picocli:picocli:4.6.1")
    // logging
    "shadedImplementation"("org.slf4j:jul-to-slf4j:1.7.30")
    "shadedImplementation"("org.slf4j:slf4j-api:1.7.+")
    "shadedImplementation"("ch.qos.logback:logback-classic:1.2.3")
    "shadedImplementation"("org.fusesource.jansi:jansi:1.18")
    "shadedImplementation"("com.google.guava:guava:30.0-jre")
//    "shadedImplementation" ("org.tuxdude.logback.extensions:logback-colorizer:1.0.1")
}

tasks.named<ShadowJar>("shadowJar") {
//    from(sourceSets["main"].allSource)
//    include("**/*.xml")
//    include("**/*.xml")
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("net.querz", "com.dfsek.terra.libs.nbt")
    minimize {
        exclude(dependency("ch.qos.logback:.*"))
        exclude(dependency("org.slf4j:.*"))
    }
}
tasks.withType<ProcessResources> {
    include("**/*.properties")
    filter<org.apache.tools.ant.filters.ReplaceTokens>(
            "tokens" to mapOf(
                    "TerraVersion" to project.version.toString(),
                    "RegionVersion" to regionVersion
            )
    )
}