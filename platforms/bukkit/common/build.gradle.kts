repositories {
    maven {
        name = "onarandombox"
        url = uri("https://repo.onarandombox.com/content/groups/public/")
    }
}

dependencies {
    shadedApi(project(":common:implementation:base"))

    compileOnly("io.papermc.paper", "paper-api", Versions.Bukkit.paper)

    compileOnly("org.mvplugins.multiverse.core", "multiverse-core", "5.0.0")

    shadedApi("io.papermc", "paperlib", Versions.Bukkit.paperLib)

    shadedApi("com.google.guava", "guava", Versions.Libraries.Internal.guava)

    shadedApi("org.incendo", "cloud-paper", Versions.Bukkit.cloud)
}
