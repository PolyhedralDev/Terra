import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.modrinth.minotaur.TaskModrinthUpload
import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("fabric-loom").version("0.12-SNAPSHOT")
    id("com.modrinth.minotaur").version("1.1.0")
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    minecraft("com.mojang:minecraft:${Versions.Fabric.minecraft}")
    mappings("net.fabricmc:yarn:${Versions.Fabric.yarn}:v2")
    
    modImplementation("net.fabricmc:fabric-loader:${Versions.Fabric.fabricLoader}")
    
    setOf("fabric-lifecycle-events-v1", "fabric-resource-loader-v0", "fabric-api-base").forEach { apiModule ->
        val module = fabricApi.module(apiModule, Versions.Fabric.fabricAPI)
        modImplementation(module)
        include(module)
    }
    
    include(modImplementation("me.lucko", "fabric-permissions-api", Versions.Fabric.permissionsAPI))
    include("me.lucko", "fabric-permissions-api", Versions.Fabric.permissionsAPI)
    
    //include(modImplementation("cloud.commandframework", "cloud-fabric", Versions.Libraries.cloud))
    //include("cloud.commandframework", "cloud-fabric", Versions.Libraries.cloud)
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