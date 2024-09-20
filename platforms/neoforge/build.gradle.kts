import java.util.*

plugins {
    id("dev.architectury.loom") version Versions.Mod.architecuryLoom
    id("architectury-plugin") version Versions.Mod.architecturyPlugin
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    "forgeRuntimeLibrary"(project(":common:implementation:base"))

    implementation(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    "developmentNeoForge"(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-common", configuration = "transformProductionNeoForge")) { isTransitive = false }

    minecraft("com.mojang", "minecraft", Versions.Mod.minecraft)
    mappings(
        loom.layered {
                mappings("net.fabricmc:yarn:${Versions.Mod.yarn}:v2")
                mappings("dev.architectury:yarn-mappings-patch-neoforge:${Versions.NeoForge.yarnPatch}")
            }
    )

    neoForge("net.neoforged", "neoforge", Versions.NeoForge.neoForge)

    modImplementation("org.incendo", "cloud-neoforge", Versions.NeoForge.cloud)
    include("org.incendo", "cloud-neoforge", Versions.NeoForge.cloud)

    //forge is not ok.
    compileOnly("org.burningwave:core:${Versions.NeoForge.burningwave}")
    "forgeRuntimeLibrary"("org.burningwave:core:${Versions.NeoForge.burningwave}")
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("src/main/resources/terra.accesswidener"))

//    mixin {
//        defaultRefmapName.set("terra.neoforge.refmap.json")
//    }
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
        dependsOn("installAddons")

        injectAccessWidener.set(true)
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("${rootProject.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}-neoforge-${project.version}.jar")
    }
}