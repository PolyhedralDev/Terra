plugins {
    id("org.spongepowered.plugin").version("0.9.0")
    id("org.spongepowered.gradle.vanilla").version("0.2")
}

repositories {
    maven {
        url = uri("https://repo-new.spongepowered.org/repository/maven-public/")
    }
}

dependencies {
    "shadedApi"(project(":common:implementation"))
    annotationProcessor(implementation("org.spongepowered:spongeapi:9.0.0-SNAPSHOT")!!)
    annotationProcessor("org.spongepowered:mixin:0.8.2:processor")
}

sponge {
    plugin {
        id = "terra"
    }
}

minecraft {
    version("1.17.1")
    runs {
        server()
        client()
    }
}

tasks.named<Jar>("jar") {
    manifest {
        attributes(
                mapOf("MixinConfigs" to "terra.mixins.json")
        )
    }
}