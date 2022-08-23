plugins {
    `kotlin-dsl`
    kotlin("jvm") version embeddedKotlinVersion
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.codemc.org/repository/maven-public") {
        name = "CodeMC"
    }
}

dependencies {
    implementation(libs.libraries.internal.shadow)
    implementation(libs.libraries.internal.error.prone.gradle)
    implementation(libs.libraries.internal.nullaway.gradle)
    
    implementation(libs.libraries.internal.asm)
    implementation(libs.libraries.internal.asm.tree)
    implementation(libs.libraries.tectonic)
    implementation(libs.libraries.snakeyaml)
}