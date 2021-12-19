import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = version("0.1.0")

plugins {
    id("com.github.johnrengelman.shadow")
}

repositories {
    maven { url = uri("https://jitpack.io/") }
}

dependencies {
    shadedApi("commons-io:commons-io:2.6")
    shadedApi("com.github.Querz:NBT:6.1")
    shadedApi(project(":common:addons:manifest-addon-loader"))
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    relocate("org.apache.commons", "com.dfsek.terra.addons.sponge.lib.commons")
}

tasks.named("build") {
    finalizedBy(tasks.named("shadowJar"))
}