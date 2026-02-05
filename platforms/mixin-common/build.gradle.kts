plugins {
    id("dev.architectury.loom") version Versions.Mod.architecuryLoom
    id("architectury-plugin") version Versions.Mod.architecturyPlugin
}

loom {
    accessWidenerPath.set(file("src/main/resources/terra.accesswidener"))

    mixin {
        useLegacyMixinAp.set(true)
        defaultRefmapName.set("terra.common.refmap.json")
    }
}

dependencies {
    shadedApi(project(":common:implementation:base"))

    compileOnly("net.fabricmc:sponge-mixin:${Versions.Mod.mixin}")
    annotationProcessor("net.fabricmc:sponge-mixin:${Versions.Mod.mixin}")
    annotationProcessor("dev.architectury:architectury-loom:${Versions.Mod.architecuryLoom}")

    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")
    mappings(loom.layered {
        mappings("net.fabricmc:yarn:${Versions.Mod.yarn}:v2")
        mappings("dev.architectury:yarn-mappings-patch-neoforge:${Versions.Mod.yarnMappingsPatchNeoForge}")
    })
}

architectury {
    common("fabric", "neoforge")
    minecraft = Versions.Mod.minecraft
}

