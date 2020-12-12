plugins {
    java
    maven
    idea
    id("com.github.johnrengelman.shadow")
}

group = "com.dfsek.terra.bukkit"

repositories {
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("http://maven.enginehub.org/repo/") }
    maven { url = uri("https://repo.codemc.org/repository/maven-public") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    implementation(project(":common"))
    implementation("org.apache.commons:commons-rng-core:1.3")

    compileOnly("org.spigotmc:spigot-api:1.16.2-R0.1-SNAPSHOT")
    implementation("io.papermc:paperlib:1.0.5")

    implementation("com.scireum:parsii:1.2.1")
    implementation("com.dfsek:Tectonic:1.0.3")
    implementation("net.jafama:jafama:2.3.2")

    compileOnly("com.googlecode.json-simple:json-simple:1.1")

    implementation("com.google.guava:guava:30.0-jre")
    implementation("org.bstats:bstats-bukkit:1.7")

    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.0-SNAPSHOT")

    compileOnly("org.jetbrains:annotations:20.1.0")

}

tasks.withType<ProcessResources> {
    include("**/*.yml")
    filter<org.apache.tools.ant.filters.ReplaceTokens>(
            "tokens" to mapOf(
                    "VERSION" to project.version.toString()
            )
    )
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