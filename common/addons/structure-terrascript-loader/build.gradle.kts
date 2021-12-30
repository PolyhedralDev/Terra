import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = version("0.1.0")


plugins {
    id("com.github.johnrengelman.shadow")
}

dependencies {
    shadedApi("commons-io:commons-io:2.6")
    shadedApi(project(":common:addons:manifest-addon-loader"))
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.apache.commons", "com.dfsek.terra.addons.terrascript.lib.commons")
}

tasks.named("build") {
    finalizedBy(tasks.named("shadowJar"))
}