import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = version("0.1.0")

plugins {
    id("com.github.johnrengelman.shadow")
}


dependencies {
    compileOnly(project(":common:addons:manifest-addon-loader"))
    implementation("com.dfsek", "paralithic", Versions.Libraries.paralithic)
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("com.dfsek.paralithic", "com.dfsek.terra.addons.noise.lib.paralithic")
}