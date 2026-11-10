plugins {
    id("dev.architectury.loom-no-remap") version Versions.Mod.architecturyLoom
}

// Loom here is in another classloader than minecraft-common's and must not read its extension.
extra["fabric.loom.disableProjectDependentMods"] = "true"

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("src/main/resources/terra.accesswidener"))
}

// Paper and Multiverse declare runtime-only libraries that break Loom's mod scan.
configurations.matching { it.name.endsWith("CompileDependencyResolve") }.configureEach {
    attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_API))
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

dependencies {
    shadedApi(project(":common:implementation:base"))

    // Paper runs vanilla unobfuscated and Mojang-named.
    minecraft("com.mojang:minecraft:${Versions.Bukkit.minecraft}")

    implementation(project(":platforms:minecraft-common")) { isTransitive = false }
    shaded(project(":platforms:minecraft-common")) { isTransitive = false }

    compileOnly("io.papermc.paper", "paper-api", Versions.Bukkit.paper)

    compileOnly("com.dfsek.wyck", "Wyck", Versions.Bukkit.wyck)

    compileOnly("org.mvplugins.multiverse.core", "multiverse-core", Versions.Bukkit.multiverse)

    shadedApi("com.google.guava", "guava", Versions.Libraries.Internal.guava)

    shadedApi("org.incendo", "cloud-paper", Versions.Bukkit.cloud)

    testImplementation(testFixtures(project(":platforms:minecraft-common")))
    testImplementation(project(":common:addons:minecraft-attributes"))
    testImplementation("io.papermc.paper", "paper-api", Versions.Bukkit.paper)
    testImplementation("com.dfsek.wyck", "Wyck", Versions.Bukkit.wyck)
}
