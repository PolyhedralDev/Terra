plugins {
    id("org.spongepowered.gradle.vanilla").version("0.2")
}

repositories {
    maven {
        url = uri("https://repo-new.spongepowered.org/repository/maven-public/")
    }
}

dependencies {
    api(project(":common:implementation:base"))
    
    api("org.slf4j:slf4j-api:1.8.0-beta4") {
        because("Minecraft 1.17+ includes slf4j 1.8.0-beta4, so we need to shade it for other versions.")
    }
    implementation("org.apache.logging.log4j", "log4j-slf4j18-impl", Versions.Libraries.log4j_slf4j_impl) {
        because("Minecraft 1.17+ includes slf4j 1.8.0-beta4, so we need to shade it for other versions.")
    }
    
    annotationProcessor("org.spongepowered", "spongeapi", Versions.Sponge.sponge)
    implementation("org.spongepowered", "spongeapi", Versions.Sponge.sponge)
    annotationProcessor("org.spongepowered:mixin:${Versions.Sponge.mixin}:processor")
}

minecraft {
    version(Versions.Sponge.minecraft)
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