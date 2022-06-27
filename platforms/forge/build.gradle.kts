plugins {
    id("dev.architectury.loom") version Versions.Forge.architecuryLoom
    id("architectury-plugin") version Versions.Mod.architectutyPlugin
    id("io.github.juuxel.loom-quiltflower") version Versions.Fabric.loomQuiltflower
}

architectury {
    platformSetupLoomIde()
    forge()
}

configurations {
    val common by creating
    compileClasspath.get().extendsFrom(common)
    runtimeClasspath.get().extendsFrom(common)
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    "common"(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-common", configuration = "transformProductionForge")) { isTransitive = false }
    "developmentForge"(project(":platforms:mixin-common", configuration = "namedElements")) {
        isTransitive = false
    }
    
    
    forge(group = "net.minecraftforge", name = "forge", version = Versions.Forge.forge)
    
    minecraft("com.mojang:minecraft:${Versions.Forge.minecraft}")
    mappings("net.fabricmc:yarn:${Versions.Forge.yarn}:v2")
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("src/main/resources/terra.accesswidener"))
    mixin {
        defaultRefmapName.set("terra-forge-refmap.json")
    }
    
    forge {
        convertAccessWideners.set(true)
        mixinConfig("terra.common.mixins.json")
        mixinConfig("terra.forge.mixins.json")
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
    
    shadowJar {
        exclude("fabric.mod.json")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("${rootProject.name.capitalize()}-${project.version}.jar")
    }
}
