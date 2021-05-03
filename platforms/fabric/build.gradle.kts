import com.dfsek.terra.configureCommon
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.modrinth.minotaur.TaskModrinthUpload
import net.fabricmc.loom.LoomGradleExtension
import net.fabricmc.loom.task.RemapJarTask

plugins {
    `java-library`
    `maven-publish`
    id("fabric-loom").version("0.6-SNAPSHOT")
    id("com.modrinth.minotaur").version("1.1.0")
}

configureCommon()

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.json", "com.dfsek.terra.lib.json")
    relocate("org.yaml", "com.dfsek.terra.lib.yaml")
}

group = "com.dfsek.terra.fabric"

dependencies {
    "shadedApi"(project(":common"))

    "minecraft"("com.mojang:minecraft:1.16.5")
    "mappings"("net.fabricmc:yarn:1.16.5+build.5:v2")
    "modImplementation"("net.fabricmc:fabric-loader:0.11.2")

    "modImplementation"("net.fabricmc.fabric-api:fabric-api:0.31.0+1.16")
}


configure<LoomGradleExtension> {
    accessWidener("src/main/resources/terra.accesswidener")
    refmapName = "terra.refmap.json"
}

val remapped = tasks.register<RemapJarTask>("remapShadedJar") {
    group = "fabric"
    val shadowJar = tasks.getByName<ShadowJar>("shadowJar")
    dependsOn(shadowJar)
    input.set(shadowJar.archiveFile)
    archiveFileName.set(shadowJar.archiveFileName.get().replace(Regex("-shaded\\.jar$"), "-shaded-mapped.jar"))
    addNestedDependencies.set(true)
    remapAccessWidener.set(true)
}


tasks.register<TaskModrinthUpload>("publishModrinthFabric") {
    dependsOn("remapShadedJar")
    group = "fabric"
    token = System.getenv("MODRINTH_SECRET")
    projectId = "FIlZB9L0"
    versionNumber = project.version.toString()
    uploadFile = remapped.get().archiveFile.get().asFile
    releaseType = "beta"
    addGameVersion("1.16.4")
    addGameVersion("1.16.5")
    addLoader("fabric")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks["sourcesJar"])
            artifact(tasks["jar"])
        }
    }

    repositories {
        val mavenUrl = "https://repo.codemc.io/repository/maven-releases/"

        maven(mavenUrl) {
            val mavenUsername: String? by project
            val mavenPassword: String? by project
            if (mavenUsername != null && mavenPassword != null) {
                credentials {
                    username = mavenUsername
                    password = mavenPassword
                }
            }
        }
    }
}