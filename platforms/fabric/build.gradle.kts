import java.util.*

plugins {
    id("dev.architectury.loom-no-remap") version Versions.Mod.architecturyLoom
    id("architectury-plugin") version Versions.Mod.architecturyPlugin
}

architectury {
    platformSetupLoomIde()
    loader("fabric")
}

dependencies {
    shadedApi(project(":common:implementation:base"))

    implementation(project(":platforms:mixin-common")) { isTransitive = false }
    "developmentFabric"(project(":platforms:mixin-common")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-common", configuration = "transformProductionFabric")) { isTransitive = false }
    implementation(project(":platforms:minecraft-common")) { isTransitive = false }
    "developmentFabric"(project(":platforms:minecraft-common")) { isTransitive = false }
    shaded(project(path = ":platforms:minecraft-common", configuration = "transformProductionFabric")) { isTransitive = false }
    implementation(project(":platforms:mixin-lifecycle")) { isTransitive = false }
    "developmentFabric"(project(":platforms:mixin-lifecycle")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-lifecycle", configuration = "transformProductionFabric")) { isTransitive = false }


    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")

    implementation("net.fabricmc:fabric-loader:${Versions.Mod.fabricLoader}")

    implementation("org.incendo", "cloud-fabric", Versions.Fabric.cloud) {
        exclude("me.lucko", "fabric-permissions-api")
    }
    include("org.incendo", "cloud-fabric", Versions.Fabric.cloud)

    runtimeOnly("net.fabricmc.fabric-api", "fabric-api", Versions.Fabric.fabricAPI)
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("src/main/resources/terra.accesswidener"))

    // Minecraft 26.x is unobfuscated: no remapJar, the shaded jar is the release jar.
    nestJars(tasks.shadowJar, configurations.include)
    injectAccessWidener(tasks.shadowJar)
}


addonDir(project.file("./run/config/Terra/addons"), tasks.named("configureLaunch").get())

tasks {
    jar {
        archiveClassifier.set("dev")
    }

    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("${rootProject.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}-fabric-${project.version}.jar")
        manifest.attributes("Fabric-Mapping-Namespace" to "official")
    }

    withType<JavaCompile>().configureEach {
        options.release = 25
    }
}
