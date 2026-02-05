plugins {
    id("dev.architectury.loom") version Versions.Mod.architecuryLoom
    id("architectury-plugin") version Versions.Mod.architecturyPlugin
}

dependencies {
    shadedApi(project(":common:implementation:base"))

    compileOnly("net.fabricmc:sponge-mixin:${Versions.Mod.mixin}")
    compileOnly("io.github.llamalad7:mixinextras-common:${Versions.Mod.mixinExtras}")
    annotationProcessor("net.fabricmc:sponge-mixin:${Versions.Mod.mixin}")
    annotationProcessor("dev.architectury:architectury-loom:${Versions.Mod.architecuryLoom}")

    implementation(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }

    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")
    mappings(loom.layered {
        mappings("net.fabricmc:yarn:${Versions.Mod.yarn}:v2")
        mappings("dev.architectury:yarn-mappings-patch-neoforge:${Versions.Mod.yarnMappingsPatchNeoForge}")
    })

    modImplementation("org.incendo", "cloud-fabric", Versions.Fabric.cloud) {
        exclude("net.fabricmc")
        exclude("net.fabricmc.fabric-api")
        exclude("me.lucko", "fabric-permissions-api")
    }
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("src/main/resources/terra.accesswidener"))

    mixin {
        useLegacyMixinAp.set(true)
        defaultRefmapName.set("terra.lifecycle.refmap.json")
    }
}

tasks {
    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
    }
}

architectury {
    common("fabric", "neoforge")
    minecraft = Versions.Mod.minecraft
}
