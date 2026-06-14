rootProject.name = "Terra"


fun includeImmediateChildren(dir: File, type: String) {
    dir.walkTopDown().maxDepth(1).forEach {
        if (!it.isDirectory || !File(it, "build.gradle.kts").exists()) return@forEach
        val addonDir = it.relativeTo(file(".")).path.replace("/", ":").replace("\\", ":")
        logger.info("Including $type directory \"$addonDir\" as subproject.")
        include(addonDir)
    }
}

includeImmediateChildren(file("common/api"), "API")

includeImmediateChildren(file("common/implementation"), "implementation")

// OVERWORLD requirements plus the loaders and transitive biome query API.
val bundledAddons = listOf(
    "api-addon-loader",
    "manifest-addon-loader",
    "biome-query-api",
    "biome-provider-pipeline",
    "biome-provider-single",
    "biome-provider-extrusion",
    "chunk-generator-noise-3d",
    "config-biome",
    "config-flora",
    "config-noise-function",
    "config-ore",
    "config-palette",
    "config-distributors",
    "config-locators",
    "config-feature",
    "config-number-predicate",
    "structure-terrascript-loader",
    "structure-sponge-loader",
    "language-yaml",
    "generation-stage-feature",
    "terrascript-function-check-noise-3d",
    "palette-block-shortcut",
    "structure-block-shortcut",
    "terrascript-function-sampler",
    "locator-slant-noise-3d"
)

bundledAddons.forEach { include(":common:addons:$it") }

include(":platforms:bukkit")
include(":platforms:bukkit:common")
include(":platforms:bukkit:nms")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.solo-studios.ca/releases") {
            name = "Solo Studios"
        }
        maven("https://maven.solo-studios.ca/snapshots") {
            name = "Solo Studios"
        }
        maven("https://maven.fabricmc.net") {
            name = "Fabric Maven"
        }
        maven("https://maven.architectury.dev/") {
            name = "Architectury Maven"
        }
        maven("https://files.minecraftforge.net/maven/") {
            name = "Forge Maven"
        }
        maven("https://maven.quiltmc.org/repository/release/") {
            name = "Quilt"
        }
    }
}

// settings.gradle.kts
val isCiServer = System.getenv().containsKey("CI")
// Cache build artifacts, so expensive operations do not need to be re-computed
buildCache {
    local {
        isEnabled = !isCiServer
    }
}
