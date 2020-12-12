plugins {
    java
    maven
    idea
    id("com.github.johnrengelman.shadow")
}

group = "com.dfsek.terra.common"

repositories {
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("http://maven.enginehub.org/repo/") }
    maven { url = uri("https://repo.codemc.org/repository/maven-public") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    compileOnly("org.jetbrains:annotations:20.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    implementation("org.apache.commons:commons-rng-core:1.3")
    implementation("commons-io:commons-io:2.4")

    implementation("com.scireum:parsii:1.2.1")
    implementation("com.dfsek:Tectonic:1.0.3")
    implementation("net.jafama:jafama:2.3.2")

    compileOnly("com.googlecode.json-simple:json-simple:1.1")

    implementation("com.google.guava:guava:30.0-jre")
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {

    archiveClassifier.set("")
    archiveBaseName.set("Terra")
    setVersion(project.version)
    relocate("org.apache.commons", "com.dfsek.terra.lib.commons")
    relocate("org.bstats.bukkit", "com.dfsek.terra.lib.bstats")
    relocate("parsii", "com.dfsek.terra.lib.parsii")
    relocate("io.papermc.lib", "com.dfsek.terra.lib.paperlib")
    relocate("net.jafama", "com.dfsek.terra.lib.jafama")
    relocate("com.dfsek.tectonic", "com.dfsek.terra.lib.tectonic")
    relocate("net.jafama", "com.dfsek.terra.lib.jafama")
    minimize()
}