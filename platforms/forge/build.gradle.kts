plugins {
    id("dev.architectury.loom") version Versions.Forge.architecuryLoom
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    forge(group = "net.minecraftforge", name = "forge", version = Versions.Forge.forge)
    
    minecraft("com.mojang:minecraft:${Versions.Forge.minecraft}")
    mappings("net.fabricmc:yarn:${Versions.Forge.yarn}:v2")
}

loom {
    mixin {
        defaultRefmapName.set("terra-refmap.json")
    }
    
    forge {
        mixinConfigs.set(listOf("terra.mixins.json"))
    }
}



tasks {
    jar {
        manifest {
            attributes(
                mapOf(
                    "Implementation-Title" to rootProject.name,
                    "Implementation-Version" to project.version,
                     )
                      )
        }
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("${rootProject.name.capitalize()}-${project.version}.jar")
    }
}
