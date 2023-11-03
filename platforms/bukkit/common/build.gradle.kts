repositories {

}

dependencies {
    shadedApi(project(":common:implementation:base"))

    compileOnly("io.papermc.paper", "paper-api", Versions.Bukkit.paper)

    shadedApi("io.papermc", "paperlib", Versions.Bukkit.paperLib)
    shadedApi("com.tcoded", "FoliaLib", Versions.Bukkit.foliaLib)
    shadedApi("com.google.guava", "guava", Versions.Libraries.Internal.guava)

    shadedApi("cloud.commandframework", "cloud-paper", Versions.Libraries.cloud)
}
