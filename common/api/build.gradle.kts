dependencies {
    shadedApi("ca.solo-studios", "strata", Versions.Libraries.strata)
    shadedApi("org.slf4j", "slf4j-api", Versions.Libraries.slf4j)
    shadedApi("cloud.commandframework", "cloud-core", Versions.Libraries.cloud)
    
    shadedApi("com.dfsek", "paralithic", Versions.Libraries.paralithic)
    shadedApi("com.dfsek.tectonic", "common", Versions.Libraries.tectonic)
    
    
    shadedImplementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
}