plugins {
    id("org.quiltmc.loom") version Versions.Quilt.loom
    id("architectury-plugin") version Versions.Mod.architectutyPlugin
}


configurations {
    val common by creating
    compileClasspath.get().extendsFrom(common)
    runtimeClasspath.get().extendsFrom(common)
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    "common"(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-common", configuration = "transformProductionQuilt")) { isTransitive = false }
    "common"(project(path = ":platforms:mixin-lifecycle", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-lifecycle", configuration = "transformProductionQuilt")) { isTransitive = false }
    
    
    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")
    mappings("net.fabricmc:yarn:${Versions.Mod.yarn}:v2")
    
    modImplementation("org.quiltmc:quilt-loader:${Versions.Quilt.quiltLoader}")
    
    modApi("org.quiltmc.quilted-fabric-api:quilted-fabric-api:${Versions.Quilt.fabricApi}")
    
    setOf(
        "fabric-lifecycle-events-v1",
        "fabric-resource-loader-v0",
        "fabric-api-base",
        "fabric-command-api-v2",
        "fabric-networking-api-v1"
         ).forEach { apiModule ->
        val module = fabricApi.module(apiModule, Versions.Fabric.fabricAPI)
        modImplementation(module)
        include(module)
    }
    
    modImplementation("cloud.commandframework", "cloud-fabric", Versions.Libraries.cloud)
    include("cloud.commandframework", "cloud-fabric", Versions.Libraries.cloud)
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("terra.accesswidener"))
    
    mixin {
        defaultRefmapName.set("terra.quilt.refmap.json")
    }
}


addonDir(project.file("./run/config/Terra/addons"), tasks.named("configureLaunch").get())

tasks {
    compileJava {
        options.release.set(17)
    }
    
    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("${rootProject.name.capitalize()}-${project.version}.jar")
    }
    
    processResources {
        from(project(":platforms:mixin-common").file("terra.accesswidener"))
        from(project(":platforms:mixin-common").file("src/main/resources/terra.common.mixins.json"))
        from(project(":platforms:mixin-lifecycle").file("src/main/resources/terra.lifecycle.mixins.json"))
    }
}
