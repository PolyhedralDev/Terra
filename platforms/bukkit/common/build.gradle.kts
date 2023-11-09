repositories {

}

dependencies {
    shadedApi(project(":common:implementation:base"))

    compileOnly("io.papermc.paper", "paper-api", Versions.Bukkit.paper)

    shadedApi("io.papermc", "paperlib", Versions.Bukkit.paperLib)
    // TODO: 2023-11-08 When we drop support for 1.18 and 1.19, we can remove FoliaLib and instead use `RegionScheduler`,
    //       AsyncScheduler, or GlobalRegionScheduler.
    shadedApi("com.tcoded", "FoliaLib", Versions.Bukkit.foliaLib)
    shadedApi("com.google.guava", "guava", Versions.Libraries.Internal.guava)

    shadedApi("cloud.commandframework", "cloud-paper", Versions.Libraries.cloud)
}
