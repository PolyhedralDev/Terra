import com.dfsek.terra.configureCommon

plugins {
    `java-library`
}

configureCommon()

group = "com.dfsek.terra.common"

dependencies {
    api("org.apache.commons:commons-rng-core:1.3")
    api("commons-io:commons-io:2.4")

    api("com.scireum:parsii:1.2.1")
    api("com.dfsek:Tectonic:1.0.3")
    api("net.jafama:jafama:2.3.2")
    api("org.yaml:snakeyaml:1.27")

    compileOnly("com.googlecode.json-simple:json-simple:1.1")

    api("com.google.guava:guava:30.0-jre")
}