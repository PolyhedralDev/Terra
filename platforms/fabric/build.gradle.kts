import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.modrinth.minotaur.TaskModrinthUpload
import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("fabric-loom") version Versions.Fabric.loom
    id("com.modrinth.minotaur") version Versions.Fabric.minotaur
    id("io.github.juuxel.loom-quiltflower") version Versions.Fabric.loomQuiltflower
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    "compileOnly"("net.fabricmc:sponge-mixin:${Versions.Fabric.mixin}")
    "annotationProcessor"("net.fabricmc:sponge-mixin:${Versions.Fabric.mixin}")
    "annotationProcessor"("net.fabricmc:fabric-loom:${Versions.Fabric.loom}")
    
    minecraft("com.mojang:minecraft:${Versions.Fabric.minecraft}")
    mappings("net.fabricmc:yarn:${Versions.Fabric.yarn}:v2")
    
    modImplementation("net.fabricmc:fabric-loader:${Versions.Fabric.fabricLoader}")
    
    setOf("fabric-lifecycle-events-v1", "fabric-resource-loader-v0", "fabric-api-base", "fabric-command-api-v2").forEach { apiModule ->
        val module = fabricApi.module(apiModule, Versions.Fabric.fabricAPI)
        modImplementation(module)
        include(module)
    }
    
    modImplementation("cloud.commandframework", "cloud-fabric", Versions.Libraries.cloud)
    include("cloud.commandframework", "cloud-fabric", Versions.Libraries.cloud)
}

loom {
    accessWidenerPath.set(file("src/main/resources/terra.accesswidener"))
    mixin {
        defaultRefmapName.set("terra-refmap.json")
    }
}



addonDir(project.file("./run/config/Terra/addons"), tasks.named("runClient").get())
addonDir(project.file("./run/config/Terra/addons"), tasks.named("runServer").get())

tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
}

tasks.getByName<ShadowJar>("shadowJar") {
    exclude("org/slf4j/**")
}

val remapped = tasks.register<RemapJarTask>("remapShadedJar") {
    dependsOn("installAddons")
    group = "fabric"
    val shadowJar = tasks.getByName<ShadowJar>("shadowJar")
    dependsOn(shadowJar)
    inputFile.set(shadowJar.archiveFile)
    archiveFileName.set(shadowJar.archiveFileName.get().replace(Regex("-shaded\\.jar$"), "-shaded-mapped.jar"))
    addNestedDependencies.set(true)
}

tasks.named("assemble").configure {
    dependsOn("remapShadedJar")
}

tasks.withType<Jar> {
    finalizedBy(remapped)
}

tasks.register<TaskModrinthUpload>("publishModrinth") {
    dependsOn("remapShadedJar")
    group = "fabric"
    token = System.getenv("MODRINTH_SECRET")
    projectId = "FIlZB9L0"
    versionNumber = "${project.version}-fabric"
    uploadFile = remapped.get().archiveFile.get().asFile
    releaseType = "beta"
    addGameVersion(Versions.Fabric.minecraft)
    addLoader("fabric")
}