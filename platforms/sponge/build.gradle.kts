plugins {
    id("org.spongepowered.gradle.vanilla").version("0.2")
}

repositories {
    maven {
        url = uri("https://repo-new.spongepowered.org/repository/maven-public/")
    }
}

dependencies {
    "shadedApi"(project(":common:implementation"))
    
    shadedImplementation("org.slf4j:slf4j-log4j12:1.7.32")
//    "shadedImplementation"("org.apache.logging.log4j:log4j-slf4j-impl:2.8.1")
    
    "annotationProcessor"("org.spongepowered:spongeapi:9.0.0-SNAPSHOT")
    "shadedImplementation"("org.spongepowered:spongeapi:9.0.0-SNAPSHOT")
    "annotationProcessor"("org.spongepowered:mixin:0.8.2:processor")
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