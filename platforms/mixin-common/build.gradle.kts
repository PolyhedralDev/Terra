plugins {
    id("dev.architectury.loom") version Versions.Mod.architecuryLoom
    id("architectury-plugin") version Versions.Mod.architecturyPlugin
    id("io.github.juuxel.loom-quiltflower") version Versions.Mod.loomQuiltflower
}

loom {
    accessWidenerPath.set(file("terra.accesswidener"))
    
    mixin {
        defaultRefmapName.set("terra.common.refmap.json")
    }
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    modImplementation("net.fabricmc:fabric-loader:${Versions.Mod.fabricLoader}")
    
    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")
    mappings("net.fabricmc:yarn:${Versions.Mod.yarn}:v2")
}

architectury {
    common("fabric", "forge", "quilt")
    minecraft = Versions.Mod.minecraft
}

