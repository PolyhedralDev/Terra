version = version("1.2.0")

dependencies {
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
    
    implementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
    testImplementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    relocate("net.jafama", "com.dfsek.terra.addons.chunkgenerator.lib.jafama")
}
