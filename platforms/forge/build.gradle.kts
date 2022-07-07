plugins {
    id("dev.architectury.loom") version Versions.Mod.architecuryLoom
    id("architectury-plugin") version Versions.Mod.architecturyPlugin
    id("io.github.juuxel.loom-quiltflower") version Versions.Mod.loomQuiltflower
}

architectury {
    platformSetupLoomIde()
    loader("forge")
}

dependencies {
    annotationProcessor("net.fabricmc:sponge-mixin:${Versions.Mod.mixin}")
    annotationProcessor("dev.architectury:architectury-loom:${Versions.Mod.architecuryLoom}")
    
    shadedApi(project(":common:implementation:base"))
    "forgeRuntimeLibrary"(project(":common:implementation:base"))
    
    implementation(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    "developmentForge"(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-common", configuration = "transformProductionForge")) { isTransitive = false }
    
    forge(group = "net.minecraftforge", name = "forge", version = Versions.Forge.forge)
    
    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")
    mappings("net.fabricmc:yarn:${Versions.Mod.yarn}:v2")
    
    //forge is not ok.
    compileOnly("org.burningwave:core:${Versions.Forge.burningwave}")
    "forgeRuntimeLibrary"("org.burningwave:core:${Versions.Forge.burningwave}")
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("src/main/resources/terra.accesswidener"))
    
    mixin {
        defaultRefmapName.set("terra.forge.refmap.json")
    }
    
    launches {
        named("client") {
            property("fabric.log.level", "debug")
            property("mixin.env.disableRefMap", "true")
        }
        named("server") {
            property("fabric.log.level", "debug")
            property("mixin.env.disableRefMap", "true")
        }
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
    
    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("${rootProject.name.capitalize()}-forge-${project.version}.jar")
    }
}