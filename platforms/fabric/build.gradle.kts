import com.dfsek.terra.configureCommon

plugins {
    id("fabric-loom").version("0.5-SNAPSHOT")
    `java-library`
}

configureCommon()

group = "com.dfsek.terra.fabric"


minecraft {
    accessWidener("src/main/resources/terra.accesswidener")
}

dependencies {
    "shadedApi"(project(":common"))


    // To change the versions see the gradle.properties file
    "minecraft"("com.mojang:minecraft:1.16.4")
    "mappings"("net.fabricmc:yarn:1.16.4+build.6:v2")
    "modImplementation"("net.fabricmc:fabric-loader:0.10.6+build.214")

    // Fabric API. This is technically optional, but you probably want it anyway.
    "modImplementation"("net.fabricmc.fabric-api:fabric-api:0.25.1+build.416-1.16")

    "compileOnly"("net.fabricmc:sponge-mixin:+")
    "annotationProcessor"("net.fabricmc:sponge-mixin:+")
    "annotationProcessor"("net.fabricmc:fabric-loom:+")
}