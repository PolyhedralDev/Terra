import com.dfsek.terra.configureCommon
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    id("org.spongepowered.plugin").version("0.9.0")
}

group = "com.dfsek.terra"

configureCommon()


dependencies {
    api(project(":common"))

    compileOnly("org.spongepowered:spongeapi:7.2.0")
}

tasks.named<ShadowJar>("shadowJar") {
}