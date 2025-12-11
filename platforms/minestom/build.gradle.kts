dependencies {
    shadedApi(project(":common:implementation:base"))
    shadedApi("com.github.ben-manes.caffeine", "caffeine", Versions.Libraries.caffeine)
    shadedImplementation("com.google.guava", "guava", Versions.Libraries.Internal.guava)

    compileOnly("net.minestom", "minestom", Versions.Minestom.minestom)
}

tasks.named("jar") {
    finalizedBy("installAddonsIntoDefaultJar")
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(25)
}