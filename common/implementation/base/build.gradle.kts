dependencies {
    shadedApi(project(":common:api"))
    shadedApi(project(":common:implementation:bootstrap-addon-loader"))
    
    shadedApi("org.apache.commons:commons-rng-core:1.3")
    shadedApi("commons-io:commons-io:2.6")
    shadedImplementation("org.apache.commons:commons-text:1.9")
    
    shadedImplementation("com.dfsek.tectonic:yaml:${Versions.Libraries.tectonic}")
    
    shadedImplementation("org.yaml:snakeyaml:1.27")
    shadedImplementation("org.ow2.asm:asm:9.2")
}
