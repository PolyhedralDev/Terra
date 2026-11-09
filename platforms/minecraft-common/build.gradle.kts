plugins {
    id("dev.architectury.loom-no-remap") version Versions.Mod.architecturyLoom
    id("architectury-plugin") version Versions.Mod.architecturyPlugin
    `java-test-fixtures`
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("src/main/resources/terra.accesswidener"))
}

dependencies {
    shadedApi(project(":common:implementation:base"))

    minecraft("com.mojang:minecraft:${Versions.Mod.minecraft}")

    // Tests only: the addon ships as its own jar.
    testImplementation(project(":common:addons:minecraft-attributes"))
    testFixturesImplementation(project(":common:addons:minecraft-attributes"))
}

architectury {
    common("fabric")
    minecraft = Versions.Mod.minecraft
}

// Loom puts Minecraft on the main and test classpaths only.
configurations.matching { it.name == "minecraftNamedCompile" }.configureEach {
    configurations["testFixturesCompileClasspath"].extendsFrom(this)
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}
