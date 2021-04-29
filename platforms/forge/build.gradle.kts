import com.dfsek.terra.configureCommon
import net.minecraftforge.gradle.userdev.UserDevExtension
import net.minecraftforge.gradle.common.util.RunConfig

buildscript {
    repositories {
        maven { url = uri("https://files.minecraftforge.net/maven") }
        jcenter()
        mavenCentral()
        maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
    }
    dependencies {
        classpath(group = "net.minecraftforge.gradle", name = "ForgeGradle", version = "4.1.+")
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}
apply(plugin = "net.minecraftforge.gradle")

plugins {
    java
}

configureCommon()

group = "com.dfsek.terra.forge"

repositories {
    maven { url = uri("https://files.minecraftforge.net/maven") }
    jcenter()
    mavenCentral()
    maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
}

val forgeVersion = "36.1.13"
val mcVersion = "1.16.5"
dependencies {
    "shadedApi"(project(":common"))
    "minecraft"("net.minecraftforge:forge:$mcVersion-$forgeVersion")
}



configure<UserDevExtension> {
    mappings(mapOf(
            "channel" to "official",
            "version" to mcVersion
    ))
    runs {
        val runConfig = Action<RunConfig> {
            properties(mapOf(
                    "forge.logging.markers" to "SCAN,REGISTRIES,REGISTRYDUMP",
                    "forge.logging.console.level" to "debug"
            ))
            workingDirectory = project.file("run").canonicalPath
            source(sourceSets["main"])
        }
        create("client", runConfig)
        create("server", runConfig)
    }
}