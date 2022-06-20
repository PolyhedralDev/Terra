import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.modrinth.minotaur.TaskModrinthUpload
import net.fabricmc.loom.task.RemapJarTask
import java.util.Date

plugins {
    id("dev.architectury.loom") version Versions.Forge.architecuryLoom
    id("com.modrinth.minotaur") version Versions.Fabric.minotaur
}

loom {
    mixin {
        defaultRefmapName.set("terra-refmap.json")
    }
    
    forge {
        mixinConfigs.set(listOf("terra.mixins.json"))
        
    }
    
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    forge("net.minecraftforge:forge:${Versions.Forge.forge}")
    
    minecraft("com.mojang:minecraft:${Versions.Forge.minecraft}")
    mappings("net.fabricmc:yarn:${Versions.Forge.yarn}:v2")
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
    group = "loom"
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
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to "terra",
                "Specification-Vendor" to "Terra Contributors",
                "Specification-Version" to "1",
                "Implementation-Title" to project.name,
                "Implementation-Version" to "@VERSION@",
                "Implementation-Vendor" to "Terra Contributors",
                "Implementation-Timestamp" to Date().toString()
                 )
                  )
    }
}

tasks.register<TaskModrinthUpload>("publishModrinth") {
    dependsOn("remapShadedJar")
    group = "loom"
    token = System.getenv("MODRINTH_SECRET")
    projectId = "FIlZB9L0"
    versionNumber = "${project.version}-forge"
    uploadFile = remapped.get().archiveFile.get().asFile
    releaseType = "beta"
    addGameVersion(Versions.Forge.minecraft)
    addLoader("forge")
}