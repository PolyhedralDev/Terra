import java.util.*

plugins {
    id("dev.architectury.loom") version Versions.Mod.architecuryLoom
    id("architectury-plugin") version Versions.Mod.architecturyPlugin
}

architectury {
    platformSetupLoomIde()
    loader("fabric")
}

repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        name = "Sonatype Snapshots"
    }
}

dependencies {
    shadedApi(project(":common:implementation:base"))

    implementation(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    "developmentFabric"(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-common", configuration = "transformProductionFabric")) { isTransitive = false }
    implementation(project(path = ":platforms:mixin-lifecycle", configuration = "namedElements")) { isTransitive = false }
    "developmentFabric"(project(path = ":platforms:mixin-lifecycle", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-lifecycle", configuration = "transformProductionFabric")) { isTransitive = false }


    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")
    mappings("net.fabricmc:yarn:${Versions.Mod.yarn}:v2")

    modImplementation("net.fabricmc:fabric-loader:${Versions.Mod.fabricLoader}")

    modImplementation("org.incendo", "cloud-fabric", Versions.Libraries.cloudFabric)
    include("org.incendo", "cloud-fabric", Versions.Libraries.cloudFabric)

    modRuntimeOnly("net.fabricmc.fabric-api", "fabric-api", Versions.Fabric.fabricAPI)
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("src/main/resources/terra.accesswidener"))

    mixin {
        defaultRefmapName.set("terra.fabric.refmap.json")
    }

}


addonDir(project.file("./run/config/Terra/addons"), tasks.named("configureLaunch").get())

tasks {
    compileJava {
        options.release.set(17)
    }

    remapJar {
        dependsOn("installAddons")

        injectAccessWidener.set(true)
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("${rootProject.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}-fabric-${project.version}.jar")
    }
}
