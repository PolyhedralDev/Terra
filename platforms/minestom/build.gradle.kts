plugins {
    application
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    shadedApi("com.github.ben-manes.caffeine", "caffeine", Versions.Libraries.caffeine)
    shadedImplementation("com.google.guava", "guava", Versions.Libraries.Internal.guava)

    compileOnly("net.minestom", "minestom-snapshots", Versions.Minestom.minestom)
}

tasks.named("jar") {
    finalizedBy("installAddonsIntoDefaultJar")
}
