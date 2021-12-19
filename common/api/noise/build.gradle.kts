dependencies {
    shadedApi(project(":common:api:util"))
    
    shadedApi("com.dfsek", "paralithic", Versions.Libraries.paralithic)
    
    shadedImplementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
}

