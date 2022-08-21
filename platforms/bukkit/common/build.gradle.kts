repositories {

}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    compileOnly(libs.bukkit.paper.api)
    
    shadedApi(libs.bukkit.paper.lib)
    
    shadedApi(libs.bukkit.cloud.paper)
}
