version = version("0.1.0")

dependencies {
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
    api("com.dfsek", "paralithic", Versions.Libraries.paralithic)
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    relocate("net.jafama", "com.dfsek.terra.addons.chunkgenerator.lib.jafama")
    relocate("com.dfsek.paralithic", "com.dfsek.terra.addons.chunkgenerator.lib.paralithic")
}
