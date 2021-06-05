import com.dfsek.terra.configureCommon

plugins {
    java
    id("org.spongepowered.plugin").version("0.9.0")
    id("org.spongepowered.gradle.vanilla").version("0.2")
}

configureCommon()

group = "com.dfsek.terra"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    "compileOnly"("org.spongepowered:spongeapi:8.0.0-SNAPSHOT")

    "annotationProcessor"("org.spongepowered:mixin:0.8.2:processor")

    "compileOnly"("org.spongepowered:mixin:0.8.2")
    "shadedApi"(project(":common"))
    "shadedImplementation"("org.yaml:snakeyaml:1.27")
    "shadedImplementation"("com.googlecode.json-simple:json-simple:1.1.1")
}

sponge {
    plugin {
        id = "terra"
    }
}

minecraft {
    version("1.16.5")
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