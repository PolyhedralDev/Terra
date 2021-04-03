import com.dfsek.terra.configureCommon

plugins {
    java
}

group = "com.dfsek.terra.minestom"

configureCommon()

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spongepowered.org/maven") }
    maven { url = uri("https://repo.velocitypowered.com/snapshots/") }
    maven { url = uri("https://libraries.minecraft.net") }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://repo.codemc.org/repository/maven-public") }
}

dependencies {
    testCompile("junit", "junit", "4.12")

    "shadedImplementation"("com.googlecode.json-simple:json-simple:1.1.1")

    "shadedImplementation"("org.apache.logging.log4j:log4j-core:2.14.1")
    "shadedImplementation"("org.apache.logging.log4j:log4j-api:2.14.1")

    "shadedImplementation"("com.github.Minestom:Minestom:4cf66fde08")
    "shadedApi"(project(":common"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.jar {
    manifest {
        attributes(
                "Main-Class" to "com.dfsek.terra.minestom.MinestomEntry",
                "Multi-Release" to "true"
        )
    }
}