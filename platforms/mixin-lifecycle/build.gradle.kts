plugins {
    id("dev.architectury.loom-no-remap") version Versions.Mod.architecturyLoom
    id("architectury-plugin") version Versions.Mod.architecturyPlugin
}

dependencies {
    shadedApi(project(":common:implementation:base"))

    compileOnly("net.fabricmc:sponge-mixin:${Versions.Mod.mixin}")
    compileOnly("io.github.llamalad7:mixinextras-common:${Versions.Mod.mixinExtras}")

    implementation(project(":platforms:mixin-common")) { isTransitive = false }
    implementation(project(":platforms:minecraft-common")) { isTransitive = false }

    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")

    implementation("org.incendo", "cloud-fabric", Versions.Fabric.cloud) {
        exclude("net.fabricmc")
        exclude("net.fabricmc.fabric-api")
        exclude("me.lucko", "fabric-permissions-api")
    }
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("src/main/resources/terra.accesswidener"))
}

architectury {
    common("fabric")
    minecraft = Versions.Mod.minecraft
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}
