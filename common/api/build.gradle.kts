import com.dfsek.terra.configureCompilation
import com.dfsek.terra.configureDependencies

plugins {
    `java-library`
    `maven-publish`
    idea
}

configureCompilation()
configureDependencies()

group = "com.dfsek.terra.common"

dependencies {
    "shadedApi"("org.apache.commons:commons-rng-core:1.3")

    "shadedApi"("com.dfsek:Paralithic:0.3.2")
    "shadedApi"("com.dfsek:Tectonic:1.4.0")

    "shadedApi"("net.jafama:jafama:2.3.2")
    "shadedApi"("org.yaml:snakeyaml:1.27")
    "shadedApi"("org.ow2.asm:asm:9.0")

    "shadedApi"("com.googlecode.json-simple:json-simple:1.1.1")

    "compileOnly"("com.google.guava:guava:30.0-jre")

    "testImplementation"("com.google.guava:guava:30.0-jre")
}

