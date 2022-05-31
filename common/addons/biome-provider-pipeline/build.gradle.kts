version = version("1.0.0")

dependencies {
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.0")
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    relocate("com.github.benmanes.caffeine", "com.dfsek.terra.addons.biome.pipeline.lib.caffeine")
}