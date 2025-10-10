repositories {
    ivy {
        url = uri("https://raw.githubusercontent.com/")
        patternLayout {
            artifact("[organisation]/[revision]/[artifact].([ext])")
            setM2compatible(true)
        }
        metadataSources {
            artifact()
        }
    }
}

val geyserMappings: Configuration by configurations.register("geyserMappings") {
    isCanBeConsumed = false
}

val mcmeta: Configuration by configurations.register("mcmeta") {
    isCanBeConsumed = false
}

dependencies {
    shadedApi(project(":common:implementation:base"))

    implementation("com.google.code.gson", "gson", Versions.Allay.gson)

    compileOnly("org.allaymc.allay", "api", Versions.Allay.api)

    geyserMappings("GeyserMC.mappings", "items", Versions.Allay.mappings, ext = "json")
    geyserMappings("GeyserMC.mappings", "biomes", Versions.Allay.mappings, ext = "json")
    geyserMappings("GeyserMC.mappings-generator", "generator_blocks", Versions.Allay.mappingsGenerator, ext = "json")

    mcmeta("misode.mcmeta", "blocks/data", Versions.Allay.mcmeta, ext = "json")
}

tasks.processResources {
    from(geyserMappings) {
        into("mapping")

        // rather jank, but whatever
        rename("(?:generator_)?([^-]+)-(.*)\\.json", "$1.json")
    }
    from(mcmeta) {
        rename("data-(.*)\\.json", "je_blocks.json")
    }
}
