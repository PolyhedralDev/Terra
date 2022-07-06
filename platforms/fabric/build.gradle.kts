plugins {
    id("dev.architectury.loom") version Versions.Mod.architecuryLoom
    id("architectury-plugin") version Versions.Mod.architecturyPlugin
    id("io.github.juuxel.loom-quiltflower") version Versions.Mod.loomQuiltflower
}

architectury {
    platformSetupLoomIde()
    loader("fabric")
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
    
    modImplementation("net.fabricmc:fabric-loader:${Versions.Fabric.fabricLoader}")
    
    setOf("fabric-lifecycle-events-v1", "fabric-resource-loader-v0", "fabric-api-base", "fabric-command-api-v2", "fabric-networking-api-v1").forEach { apiModule ->
        val module = fabricApi.module(apiModule, Versions.Fabric.fabricAPI)
        modImplementation(module)
        include(module)
    }
    
    modImplementation("cloud.commandframework", "cloud-fabric", Versions.Libraries.cloud)
    include("cloud.commandframework", "cloud-fabric", Versions.Libraries.cloud)
    
    modLocalRuntime("com.github.astei:lazydfu:${Versions.Mod.lazyDfu}")
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("src/main/resources/terra.accesswidener"))
    
    mixin {
        defaultRefmapName.set("terra.fabric.refmap.json")
    }
    
    launches {
        named("client") {
            property("fabric.log.level", "debug")
        }
        named("server") {
            property("fabric.log.level", "debug")
        }
    }
}


addonDir(project.file("./run/config/Terra/addons"), tasks.named("configureLaunch").get())

tasks {
    compileJava {
        options.release.set(17)
    }
    
    remapJar {
        injectAccessWidener.set(true)
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("${rootProject.name.capitalize()}-${project.version}.jar")
    }
}
