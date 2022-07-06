plugins {
    id("dev.architectury.loom") version Versions.Mod.architecuryLoom
    id("architectury-plugin") version Versions.Mod.architecturyPlugin
    id("io.github.juuxel.loom-quiltflower") version Versions.Mod.loomQuiltflower
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
    "forgeRuntimeLibrary"(project(":common:implementation:base"))
    
    
    "common"(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-common", configuration = "transformProductionForge")) { isTransitive = false }
    "developmentForge"(project(path = ":platforms:mixin-common", configuration = "namedElements")) {
        isTransitive = false
    }
    
    
    forge(group = "net.minecraftforge", name = "forge", version = Versions.Forge.forge)
    
    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")
    mappings("net.fabricmc:yarn:${Versions.Mod.yarn}:v2")
    
    //forge is not ok.
    compileOnly("org.burningwave:core:${Versions.Forge.burningwave}")
    "forgeRuntimeLibrary"("org.burningwave:core:${Versions.Forge.burningwave}")
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("terra.accesswidener"))
    
    mixin {
        defaultRefmapName.set("terra.forge.refmap.json")
    }
    
    forge {
        convertAccessWideners.set(true)
        mixinConfig("terra.common.mixins.json")
        mixinConfig("terra.forge.mixins.json")
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
    }
}


addonDir(project.file("./run/config/Terra/addons"), tasks.named("configureLaunch").get())

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
    
    processResources {
        from(project(":platforms:mixin-common").file("terra.accesswidener"))
    }
}