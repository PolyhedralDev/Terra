plugins {
    id("org.spongepowered.gradle.vanilla").version("0.2")
}

repositories {
    maven {
        url = uri("https://repo-new.spongepowered.org/repository/maven-public/")
    }
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    shadedApi("org.slf4j:slf4j-api:1.8.0-beta4") {
        because("Minecraft 1.17+ includes slf4j 1.8.0-beta4, so we need to shade it for other versions.")
    }
    shadedImplementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.14.1") {
        because("Minecraft 1.17+ includes slf4j 1.8.0-beta4, so we need to shade it for other versions.")
    }
    
    annotationProcessor("org.spongepowered:spongeapi:9.0.0-SNAPSHOT")
    shadedImplementation("org.spongepowered:spongeapi:9.0.0-SNAPSHOT")
    annotationProcessor("org.spongepowered:mixin:0.8.2:processor")
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
        //attributes(
        //        mapOf("MixinConfigs" to "terra.mixins.json")
        //)
    }
}