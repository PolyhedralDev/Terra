import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = version("1.0.0")

dependencies {
    api("commons-io:commons-io:2.7")
    api("com.github.Querz:NBT:6.1")
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.apache.commons", "com.dfsek.terra.addons.sponge.lib.commons")
}