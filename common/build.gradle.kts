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

    compileOnly("com.googlecode.json-simple:json-simple:1.1")

    api("com.google.guava:guava:30.0-jre")
}

//    dependencies {
//        implementation("org.apache.commons:commons-rng-core:1.3")
//        implementation("net.jafama:jafama:2.3.2")
//
//
//        implementation("commons-io:commons-io:2.4")
//        implementation("org.apache.commons:commons-imaging:1.0-alpha2")
//
//        compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.0-SNAPSHOT")
//        implementation("org.bstats:bstats-bukkit:1.7")
//
//        compileOnly("com.googlecode.json-simple:json-simple:1.1")
//
//        implementation("com.scireum:parsii:1.2.1")
//
//        implementation("net.jafama:jafama:2.3.2")
//
//        implementation("com.dfsek:Tectonic:1.0.3")
//
//
//
//
//        // JUnit.
//        testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
//        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
//    }

//tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
//
//    archiveClassifier.set("")
//    archiveBaseName.set("Terra")
//    setVersion(project.version)
//    relocate("org.apache.commons", "com.dfsek.terra.lib.commons")
//    relocate("org.bstats.bukkit", "com.dfsek.terra.lib.bstats")
//    relocate("parsii", "com.dfsek.terra.lib.parsii")
//    relocate("io.papermc.lib", "com.dfsek.terra.lib.paperlib")
//    relocate("net.jafama", "com.dfsek.terra.lib.jafama")
//    relocate("com.dfsek.tectonic", "com.dfsek.terra.lib.tectonic")
//    relocate("net.jafama", "com.dfsek.terra.lib.jafama")
//    minimize()
//}