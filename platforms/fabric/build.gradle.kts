import com.dfsek.terra.addonDir
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.modrinth.minotaur.TaskModrinthUpload
import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("fabric-loom").version("0.10.55")
    id("com.modrinth.minotaur").version("1.1.0")
}

addonDir(project.rootProject.file("./run/config/Terra/addons"), tasks.named("runClient").get())
addonDir(project.rootProject.file("./run/config/Terra/addons"), tasks.named("runServer").get())

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.json", "com.dfsek.terra.lib.json")
    relocate("org.yaml", "com.dfsek.terra.lib.yaml")
}

val minecraft = "1.18-pre5"
val yarn = "4"
val fabricLoader = "0.12.5"


dependencies {
    "shadedApi"(project(":common:implementation"))
    
    "minecraft"("com.mojang:minecraft:$minecraft")
    "mappings"("net.fabricmc:yarn:$minecraft+build.$yarn:v2")
    "modImplementation"("net.fabricmc:fabric-loader:$fabricLoader")
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
}

loom {
    accessWidenerPath.set(file("src/main/resources/terra.accesswidener"))
    mixin {
        defaultRefmapName.set("terra-refmap.json")
    }
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
