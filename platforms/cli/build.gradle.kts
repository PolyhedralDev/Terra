plugins {
    application
}

val javaMainClass = "com.dfsek.terra.cli.TerraCLI"

dependencies {
    shadedApi(project(":common:implementation:base"))

    shadedApi("commons-io", "commons-io", Versions.Libraries.Internal.apacheIO)
    shadedApi("com.github.Querz", "NBT", "Versions.CLI.nbt")

    shadedImplementation("com.google.guava", "guava", "Versions.Libraries.Internal.guava")

    shadedImplementation("ch.qos.logback", "logback-classic", "Versions.CLI.logback")


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
