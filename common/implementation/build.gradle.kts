dependencies {
    "shadedApi"(project(":common:api"))
    "shadedApi"(project(":common:loader:config"))
    "shadedApi"(project(":common:loader:addon"))
    
    "shadedApi"("org.apache.commons:commons-rng-core:1.3")
    "shadedApi"("commons-io:commons-io:2.6")
    "shadedImplementation"("org.apache.commons:commons-text:1.9")
    
    "shadedImplementation"("com.dfsek.tectonic:yaml:2.1.2")
    
    "shadedImplementation"("org.yaml:snakeyaml:1.27")
    "shadedImplementation"("org.ow2.asm:asm:9.0")
}
