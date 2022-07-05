plugins {
    id("dev.architectury.loom") version Versions.Mod.architecuryLoom
    id("architectury-plugin") version Versions.Mod.architectutyPlugin
    id("io.github.juuxel.loom-quiltflower") version Versions.Mod.loomQuiltflower
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("terra.accesswidener"))
    
    mixin {
        defaultRefmapName.set("terra.lifecycle.refmap.json")
    }
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    modImplementation("net.fabricmc:fabric-loader:${Versions.Mod.fabricLoader}")
    compileOnly("net.fabricmc:sponge-mixin:${Versions.Fabric.mixin}")
    
    compileOnly(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    
    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")
    mappings("net.fabricmc:yarn:${Versions.Mod.yarn}:v2")
}

architectury {
    common("fabric", "quilt")
    minecraft = Versions.Mod.minecraft
}

