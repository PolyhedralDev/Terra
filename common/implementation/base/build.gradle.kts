dependencies {
    shadedApi(project(":common:api"))
    shadedApi(project(":common:implementation:bootstrap-addon-loader"))
    
    shadedImplementation("commons-io", "commons-io", Versions.Libraries.Internal.apacheIO)
    
    shadedImplementation("org.apache.commons", "commons-text", Versions.Libraries.Internal.apacheText)
    shadedImplementation("com.dfsek.tectonic", "yaml", Versions.Libraries.tectonic)
    
    shadedImplementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
    shadedImplementation("org.ow2.asm", "asm", Versions.Libraries.Internal.asm)
    
    shadedImplementation("it.unimi.dsi", "fastutil:", Versions.Libraries.Internal.fastutil)
}
