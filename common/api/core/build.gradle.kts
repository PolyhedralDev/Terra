dependencies {
    shadedApi(project(":common:api:util"))
    shadedApi(project(":common:api:noise"))
    shadedApi(project(":common:api:registry"))
    shadedApi(project(":common:api:addons"))
    
    shadedApi("net.jafama:jafama:2.3.2")
    
    shadedApi("org.slf4j:slf4j-api:1.7.32")
    
    shadedApi("cloud.commandframework", "cloud-core", "1.6.1")
}

