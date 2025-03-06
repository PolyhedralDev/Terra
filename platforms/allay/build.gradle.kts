dependencies {
    shadedApi(project(":common:implementation:base"))

    implementation("com.google.code.gson", "gson", Versions.Allay.gson)

    compileOnly("org.allaymc.allay", "api", Versions.Allay.api)
}

tasks.register<Copy>("copyMappings") {
    from("src/main/resources/mappings") {
        include("biomes.json", "items.json")
    }
    from("src/main/resources/mappings-generator") {
        include("generator_blocks.json")
        rename("generator_blocks.json", "blocks.json")
    }
    into("src/main/resources/mapping")
}

tasks.processResources {
    dependsOn("copyMappings")

    exclude("mapping/.keep")
    exclude("mappings/**")
    exclude("mappings-generator/**")
}