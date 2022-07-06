plugins {
    id("dev.architectury.loom") version Versions.Mod.architecuryLoom
    id("architectury-plugin") version Versions.Mod.architecturyPlugin
    id("io.github.juuxel.loom-quiltflower") version Versions.Mod.loomQuiltflower
}

architectury {
    platformSetupLoomIde()
    fabric()
}

configurations {
    val common by creating
    compileClasspath.get().extendsFrom(common)
    runtimeClasspath.get().extendsFrom(common)
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    compileOnly("net.fabricmc:sponge-mixin:${Versions.Fabric.mixin}")
    annotationProcessor("net.fabricmc:sponge-mixin:${Versions.Fabric.mixin}")
    annotationProcessor("net.fabricmc:fabric-loom:${Versions.Fabric.loom}")
    
    "common"(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    
    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")
    mappings("net.fabricmc:yarn:${Versions.Mod.yarn}:v2")
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("terra.accesswidener"))
    
    mixin {
        defaultRefmapName.set("terra.lifecycle.refmap.json")
    }
}

tasks {
    compileJava {
        options.release.set(17)
    }
    
    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
    }
    
    processResources {
        from(project(":platforms:mixin-common").file("terra.accesswidener"))
    }
}

architectury {
    common("fabric", "quilt")
    minecraft = Versions.Mod.minecraft
}