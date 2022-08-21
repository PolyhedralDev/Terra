plugins {
    application
}

val javaMainClass = "com.dfsek.terra.cli.TerraCLI"

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    shadedApi(libs.libraries.internal.apache.io)
    shadedApi(libs.cli.nbt)
    
    shadedImplementation(libs.cli.logback)
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
