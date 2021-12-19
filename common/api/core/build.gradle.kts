import com.dfsek.terra.Versions

dependencies {
    shadedApi(project(":common:api:util"))
    shadedApi(project(":common:api:noise"))
    shadedApi(project(":common:api:registry"))
    shadedApi(project(":common:api:addons"))
    
    shadedApi("net.jafama:jafama:2.3.2")
    
    shadedApi("org.slf4j", "slf4j-api", Versions.Libraries.slf4j)
    
    shadedApi("cloud.commandframework", "cloud-core", "1.6.1")
}

