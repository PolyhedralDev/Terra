import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = version("1.1.0")

dependencies {
    api("commons-io:commons-io:2.7")
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
    implementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
    testImplementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.apache.commons", "com.dfsek.terra.addons.terrascript.lib.commons")
    relocate("net.jafama", "com.dfsek.terra.addons.terrascript.lib.jafama")
}