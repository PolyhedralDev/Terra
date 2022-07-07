repositories {

}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    api("org.slf4j:slf4j-api:1.8.0-beta4") {
        because("Minecraft 1.17+ includes slf4j 1.8.0-beta4, so we need to shade it for other versions.")
    }
    
    compileOnly("io.papermc.paper:paper-api:${Versions.Bukkit.paper}")
    
    shadedApi("io.papermc", "paperlib", Versions.Bukkit.paperLib)
    shadedApi("com.google.guava:guava:30.0-jre")
    
    shadedApi("cloud.commandframework", "cloud-paper", Versions.Libraries.cloud)
}
