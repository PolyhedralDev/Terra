plugins {
    alias(libs.plugins.mod.architectury.loom)
    alias(libs.plugins.mod.architectury.plugin)
    alias(libs.plugins.mod.loom.quiltflower)
}

architectury {
    platformSetupLoomIde()
    loader("forge")
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    "forgeRuntimeLibrary"(project(":common:implementation:base"))
    
    implementation(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    "developmentForge"(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-common", configuration = "transformProductionForge")) { isTransitive = false }
    
    compileOnly(libs.mod.mixin)
    compileOnly(libs.mod.fabric.fabric.loader)
    
    errorprone(libs.mod.mixin)
    errorprone(libs.mod.fabric.fabric.loader)
    
    forge(libs.mod.forge.forge)
    
    minecraft(libs.mod.minecraft)
    mappings("net.fabricmc", "yarn", libs.versions.mod.yarn.get(), classifier = "v2")
    
    //forge is not ok.
    compileOnly(libs.mod.forge.burningwave)
    "forgeRuntimeLibrary"(libs.mod.forge.burningwave)
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