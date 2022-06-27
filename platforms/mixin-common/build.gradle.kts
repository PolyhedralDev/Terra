plugins {
    id("dev.architectury.loom") version Versions.Mod.architecuryLoom
    id("architectury-plugin") version Versions.Mod.architectutyPlugin
}

loom {
    mixin {
        defaultRefmapName.set("terra-common-refmap.json")
    }
    accessWidenerPath.set(file("src/main/resources/terra.accesswidener"))
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    modImplementation("net.fabricmc:fabric-loader:${Versions.Mod.fabricLoader}")
    
    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")
    mappings("net.fabricmc:yarn:${Versions.Mod.yarn}:v2")
}

architectury {
    common("fabric", "forge")
    minecraft = Versions.Mod.minecraft
}