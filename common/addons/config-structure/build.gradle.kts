version = version("1.0.0")

dependencies {
    api("com.googlecode.json-simple:json-simple:1.1.1")
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
    
    implementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
    testImplementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    relocate("net.jafama", "com.dfsek.terra.addons.structure.lib.jafama")
}
