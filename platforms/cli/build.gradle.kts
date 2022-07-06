plugins {
    application
}

val javaMainClass = "com.dfsek.terra.cli.TerraCLI"

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    shadedApi("commons-io:commons-io:${Versions.CLI.commonsIO}")
    shadedApi("com.github.Querz:NBT:${Versions.CLI.nbt}")
    
    shadedImplementation("com.google.guava:guava:${Versions.CLI.guava}")
    
    shadedImplementation("ch.qos.logback:logback-classic:${Versions.CLI.logback}")
    
    implementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
}

tasks.withType<Jar> {
    entryCompression = ZipEntryCompression.STORED
    manifest {
        attributes(
            "Main-Class" to javaMainClass,
                  )
    }
}

application {
    mainClass.set(javaMainClass)
}

tasks.getByName("run").setProperty("workingDir", file("./run"))

addonDir(project.file("./run/addons"), tasks.named("run").get())
