repositories {

}

dependencies {
    shadedApi(project(":common:implementation:base"))

    compileOnly("io.papermc.paper", "paper-api", Versions.Bukkit.paper)

    compileOnly("org.mvplugins.multiverse.core", "multiverse-core", Versions.Bukkit.multiverse)

    shadedApi("io.papermc", "paperlib", Versions.Bukkit.paperLib)

    shadedApi("com.google.guava", "guava", Versions.Libraries.Internal.guava)

    shadedApi("org.incendo", "cloud-paper", Versions.Bukkit.cloud)
}
