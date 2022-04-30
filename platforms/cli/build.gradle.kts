repositories {
    maven { url = uri("https://jitpack.io/") }
}

dependencies {
    api("commons-io:commons-io:2.6")
    api("com.github.Querz:NBT:6.1")
    api(project(":common:implementation:base"))
    
    implementation("com.google.guava:guava:31.0.1-jre")
    
    implementation("ch.qos.logback:logback-classic:1.2.7")
    
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