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

dependencies {
    shadedApi(project(":common:implementation:base"))

    implementation("com.google.code.gson", "gson", Versions.Allay.gson)

    compileOnly("org.allaymc.allay", "api", Versions.Allay.api)

    geyserMappings("GeyserMC.mappings:items:3626653@json")
    geyserMappings("GeyserMC.mappings:biomes:3626653@json")
    geyserMappings("GeyserMC.mappings-generator:generator_blocks:366618e@json")
}

tasks.processResources {
    from(geyserMappings) {
        into("mapping")

        // rather jank, but whatever
        rename("(?:generator_)?([^-]+)-(.*)\\.json", "$1.json")
    }
}
