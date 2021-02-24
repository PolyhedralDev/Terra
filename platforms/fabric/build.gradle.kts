import com.dfsek.terra.configureCommon
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.LoomGradleExtension
import net.fabricmc.loom.task.RemapJarTask

buildscript {
    repositories {
        mavenCentral()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
    }
//    dependencies {
//        classpath("net.fabricmc:fabric-loom:+")
//    }
}

plugins {
    id("fabric-loom").version("0.5.9")
    `java-library`
}
//apply(plugin = "fabric-loom")

configureCommon()

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.json", "com.dfsek.terra.lib.json")
    relocate("org.yaml", "com.dfsek.terra.lib.yaml")
}

group = "com.dfsek.terra.fabric"


configure<LoomGradleExtension> {
    accessWidener("src/main/resources/terra.accesswidener")
}

tasks.register<RemapJarTask>("remapShadedJar") {
    dependsOn("shadowJar")
    setProperty("input", file("build/libs/Terra-fabric-${version}-shaded.jar"))
    setProperty("addNestedDependencies", false)
    setProperty("remapAccessWidener", true)
}

dependencies {
    "shadedApi"(project(":common"))
    "shadedImplementation"("org.yaml:snakeyaml:1.27")
    "shadedImplementation"("com.googlecode.json-simple:json-simple:1.1.1")


    // To change the versions see the gradle.properties file
    "minecraft"("com.mojang:minecraft:1.16.5")
    "mappings"("net.fabricmc:yarn:1.16.5+build.5:v2")
    "modImplementation"("net.fabricmc:fabric-loader:0.11.2")

    // Fabric API. This is technically optional, but you probably want it anyway.
    "modImplementation"("net.fabricmc.fabric-api:fabric-api:0.31.0+1.16")

    "compileOnly"("net.fabricmc:sponge-mixin:+")
    "annotationProcessor"("net.fabricmc:sponge-mixin:+")
    "annotationProcessor"("net.fabricmc:fabric-loom:+")
}