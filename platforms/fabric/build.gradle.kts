import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.modrinth.minotaur.TaskModrinthUpload
import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("fabric-loom").version("0.10.58")
    id("com.modrinth.minotaur").version("1.1.0")
}

// Do not shade because minecraft already includes it
dependencies {
    shadedApi(project(":common:implementation:base"))
    
    shadedApi("org.slf4j", "slf4j-api", Versions.Libraries.slf4j) {
        because("Minecraft 1.17+ includes slf4j 1.8.0-beta4, but we want slf4j 1.7.")
    }
    shaded("org.apache.logging.log4j", "log4j-slf4j-impl", Versions.Libraries.log4j_slf4j_impl) {
        because("Minecraft 1.17+ includes slf4j 1.8.0-beta4, but we want slf4j 1.7.")
        exclude("org.apache.logging.log4j")
    }
    
    minecraft("com.mojang:minecraft:${Versions.Fabric.minecraft}") {
        exclude("org.slf4j")
    }
    mappings("net.fabricmc:yarn:${Versions.Fabric.yarn}:v2")
    modImplementation("net.fabricmc:fabric-loader:${Versions.Fabric.fabricLoader}")
    
    
    setOf("fabric-command-api-v1", "fabric-lifecycle-events-v1").forEach { apiModule ->
        modImplementation(fabricApi.module(apiModule, Versions.Fabric.fabricAPI))?.let { include(it) }
    }
    
    include(modImplementation("me.lucko", "fabric-permissions-api", Versions.Fabric.permissionsAPI))
    include(modImplementation("cloud.commandframework", "cloud-fabric", Versions.Libraries.cloud))
}

loom {
    accessWidenerPath.set(file("src/main/resources/terra.accesswidener"))
    mixin {
        defaultRefmapName.set("terra-refmap.json")
    }
}


addonDir(project.rootProject.file("./run/config/Terra/addons"), tasks.named("runClient").get())
addonDir(project.rootProject.file("./run/config/Terra/addons"), tasks.named("runServer").get())

tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
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