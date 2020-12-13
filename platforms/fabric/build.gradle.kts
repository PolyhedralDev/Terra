plugins {
    java
    maven
    idea
    id("fabric-loom").version("0.5-SNAPSHOT")
    id("com.github.johnrengelman.shadow")
}

group = "com.dfsek.terra.bukkit"

repositories {
    mavenCentral()
    maven { url = uri("http://maven.enginehub.org/repo/") }
    maven { url = uri("https://repo.codemc.org/repository/maven-public") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    jcenter()
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
    gradlePluginPortal()
}


minecraft {
    accessWidener("src/main/resources/terra.accesswidener")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    implementation(project(":common"))
    implementation("org.apache.commons:commons-rng-core:1.3")


    implementation("com.scireum:parsii:1.2.1")
    implementation("com.dfsek:Tectonic:1.0.3")
    implementation("net.jafama:jafama:2.3.2")

    implementation("com.googlecode.json-simple:json-simple:1.1")

    implementation("com.google.guava:guava:30.0-jre")

    compileOnly("org.jetbrains:annotations:20.1.0")

    implementation("org.yaml:snakeyaml:1.27")

    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:1.16.4")
    mappings("net.fabricmc:yarn:1.16.4+build.6:v2")
    modImplementation("net.fabricmc:fabric-loader:0.10.6+build.214")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.25.1+build.416-1.16")
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {

    archiveClassifier.set("")
    archiveBaseName.set("Terra")
    setVersion(project.version)
    relocate("org.apache.commons", "com.dfsek.terra.lib.commons")
    relocate("parsii", "com.dfsek.terra.lib.parsii")
    relocate("net.jafama", "com.dfsek.terra.lib.jafama")
    relocate("com.dfsek.tectonic", "com.dfsek.terra.lib.tectonic")
    minimize()
}