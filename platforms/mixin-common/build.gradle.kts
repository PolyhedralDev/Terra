plugins {
    id("dev.architectury.loom-no-remap") version Versions.Mod.architecturyLoom
    id("architectury-plugin") version Versions.Mod.architecturyPlugin
}

loom {
    accessWidenerPath.set(file("src/main/resources/terra.accesswidener"))
}

dependencies {
    shadedApi(project(":common:implementation:base"))

    compileOnly("net.fabricmc:sponge-mixin:${Versions.Mod.mixin}")

    api(project(":platforms:minecraft-common")) { isTransitive = false }

    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")
}

architectury {
    common("fabric")
    minecraft = Versions.Mod.minecraft
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}
