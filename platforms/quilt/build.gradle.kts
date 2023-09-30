plugins {
    id("dev.architectury.loom") version Versions.Mod.architecuryLoom
    id("architectury-plugin") version Versions.Mod.architecturyPlugin
    id("io.github.juuxel.loom-quiltflower") version Versions.Mod.loomQuiltflower
}

architectury {
    platformSetupLoomIde()
    loader("quilt")
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    annotationProcessor("net.fabricmc:sponge-mixin:${Versions.Mod.mixin}")
    annotationProcessor("dev.architectury:architectury-loom:${Versions.Mod.architecuryLoom}")
    
    
    implementation(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    "developmentQuilt"(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-common", configuration = "transformProductionQuilt")) { isTransitive = false }
    
    implementation(project(path = ":platforms:mixin-lifecycle", configuration = "namedElements")) { isTransitive = false }
    "developmentQuilt"(project(path = ":platforms:mixin-lifecycle", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-lifecycle", configuration = "transformProductionQuilt")) { isTransitive = false }
    
    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")
    mappings("net.fabricmc:yarn:${Versions.Mod.yarn}:v2")
    
    modImplementation("org.quiltmc:quilt-loader:${Versions.Quilt.quiltLoader}")
    
    modImplementation("org.quiltmc.quilted-fabric-api:quilted-fabric-api:${Versions.Quilt.fabricApi}")
    
    modImplementation("cloud.commandframework", "cloud-fabric", Versions.Libraries.cloud) {
        exclude("net.fabricmc")
        exclude("net.fabricmc.fabric-api")
    }
    include("cloud.commandframework", "cloud-fabric", Versions.Libraries.cloud) {
        exclude("net.fabricmc")
        exclude("net.fabricmc.fabric-api")
    }
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("src/main/resources/terra.accesswidener"))
    
    mixin {
        defaultRefmapName.set("terra.quilt.refmap.json")
    }
    
    launches {
        named("client") {
            property("fabric.log.level", "info")
        }
        named("server") {
            property("fabric.log.level", "info")
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
        archiveFileName.set("${rootProject.name.capitalize()}-quilt-${project.version}.jar")
    }
}
