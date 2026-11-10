plugins {
    application
}

val javaMainClass = "com.dfsek.terra.cli.TerraCLI"

dependencies {
    shadedApi(project(":common:implementation:base"))

    shadedApi("commons-io:commons-io:${Versions.Libraries.Internal.apacheIO}")
    shadedApi("com.github.Querz:NBT:${Versions.Libraries.Internal.nbt}")

    shadedImplementation("info.picocli:picocli:${Versions.CLI.picocli}")
    annotationProcessor("info.picocli:picocli-codegen:${Versions.CLI.picocli}")

    shadedImplementation("com.google.guava:guava:${Versions.Libraries.Internal.guava}")

    shadedImplementation("ch.qos.logback:logback-classic:${Versions.CLI.logback}")

    // Addon class loaders find minecraft-attributes on the test class path first; its entrypoint needs the manifest addon API.
    testImplementation(project(":common:addons:minecraft-attributes"))
    testImplementation(project(":common:addons:manifest-addon-loader"))
}

tasks.withType<Jar> {
    entryCompression = ZipEntryCompression.STORED
    manifest {
        attributes(
            "Main-Class" to javaMainClass,
        )
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Aproject=${project.group}/${project.name}")
}

application {
    mainClass.set(javaMainClass)
}

tasks.getByName("run").setProperty("workingDir", file("./run"))

// CLIPlatform uses "./" as its data folder. Terra initializes once per JVM, and each test class starts its own platform.
tasks.withType<Test> {
    workingDir = file("./run").also { it.mkdirs() }
    forkEvery = 1
}

tasks.withType<Test> { dependsOn("moveAddonsrun") }

addonDir(project.file("./run/addons"), tasks.named("run").get())
