import com.dfsek.terra.configureCommon

plugins {
    `java-library`
}

configureCommon()

group = "com.dfsek.terra.common"

dependencies {
    "shadedApi"("org.apache.commons:commons-rng-core:1.3")
    "shadedApi"("commons-io:commons-io:2.4")

    "shadedApi"("com.scireum:parsii:1.2.1")
    "shadedApi"("com.dfsek:Tectonic:1.1.0")
    "shadedApi"("net.jafama:jafama:2.3.2")
    "shadedApi"("org.yaml:snakeyaml:1.27")

    "compileOnly"("com.googlecode.json-simple:json-simple:1.1")

    "shadedApi"("com.google.guava:guava:30.0-jre")
}