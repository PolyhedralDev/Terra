repositories {
    maven { url = uri("https://jitpack.io/") }
}

dependencies {
    shadedApi("commons-io:commons-io:2.7")
    shadedApi("com.github.Querz:NBT:6.1")
    shadedApi(project(":common:implementation:base"))
    
    shadedImplementation("com.google.guava:guava:31.0.1-jre")
    
    shadedImplementation("ch.qos.logback:logback-classic:1.2.9")
    
    implementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
}

tasks.withType<Jar>() {
    entryCompression = ZipEntryCompression.STORED
    manifest {
        attributes(
            "Main-Class" to "com.dfsek.terra.cli.TerraCLI",
                  )
    }
}