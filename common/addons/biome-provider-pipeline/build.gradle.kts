version = version("1.0.1")

dependencies {
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.0")
    
    implementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
    testImplementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    relocate("com.github.benmanes.caffeine", "com.dfsek.terra.addons.biome.pipeline.lib.caffeine")
    relocate("net.jafama", "com.dfsek.terra.addons.biome.pipeline.lib.jafama")
}