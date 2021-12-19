dependencies {
    shadedApi(project(":common:api:util"))
    shadedApi(project(":common:api:noise"))
    shadedApi(project(":common:api:registry"))
    shadedApi(project(":common:api:addons"))
    
    shadedApi("org.slf4j", "slf4j-api", Versions.Libraries.slf4j)
    shadedApi("cloud.commandframework", "cloud-core", Versions.Libraries.cloud)
    
    
    shadedImplementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
}

