import com.dfsek.terra.addonDir
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.modrinth.minotaur.TaskModrinthUpload
import net.fabricmc.loom.LoomGradleExtension
import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("fabric-loom").version("0.8-SNAPSHOT")
    id("com.modrinth.minotaur").version("1.1.0")
}

val fabricApi = "0.31.0+1.16"

addonDir(project.rootProject.file("./run/config/Terra/addons"), tasks.named("runClient").get())
addonDir(project.rootProject.file("./run/config/Terra/addons"), tasks.named("runServer").get())

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.json", "com.dfsek.terra.lib.json")
    relocate("org.yaml", "com.dfsek.terra.lib.yaml")
}


dependencies {
    "shadedApi"(project(":common:implementation"))
    
    "minecraft"("com.mojang:minecraft:1.17.1")
    "mappings"("net.fabricmc:yarn:1.17.1+build.1:v2")
    "modImplementation"("net.fabricmc:fabric-loader:0.11.3")
    
    "modCompileOnly"("com.sk89q.worldedit:worldedit-fabric-mc1.16:7.2.0-SNAPSHOT") {
        exclude(group = "com.google.guava", module = "guava")
        exclude(group = "com.google.code.gson", module = "gson")
        exclude(group = "it.unimi.dsi", module = "fastutil")
        exclude(group = "org.apache.logging.log4j", module = "log4j-api")
        exclude(group = "org.apache.logging.log4j", module = "log4j-core")
    }
    
    "modImplementation"("cloud.commandframework", "cloud-fabric", "1.5.0")
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.json", "com.dfsek.terra.lib.json")
    relocate("org.yaml", "com.dfsek.terra.lib.yaml")
}


configure<LoomGradleExtension> {
    accessWidener("src/main/resources/terra.accesswidener")
    refmapName = "terra-refmap.json"
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
    versionNumber = "${project.version}-fabric"
    uploadFile = remapped.get().archiveFile.get().asFile
    releaseType = "beta"
    addGameVersion("1.16.4")
    addGameVersion("1.16.5")
    addLoader("fabric")
}
