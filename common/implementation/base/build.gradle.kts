dependencies {
    api(project(":common:api"))
    api(project(":common:implementation:bootstrap-addon-loader"))
    
    implementation("commons-io", "commons-io", Versions.Libraries.Internal.apacheIO)
    implementation("org.apache.commons", "commons-text", Versions.Libraries.Internal.apacheText)
    
    implementation("com.dfsek.tectonic", "yaml", Versions.Libraries.tectonic)
    implementation("com.dfsek.tectonic", "common", Versions.Libraries.tectonic)
    
    implementation("com.dfsek", "paralithic", Versions.Libraries.paralithic)
    
    implementation("ca.solo-studios", "strata", Versions.Libraries.strata)
    
    implementation("cloud.commandframework", "cloud-core", Versions.Libraries.cloud)
    
    implementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
    
    implementation("org.ow2.asm", "asm", Versions.Libraries.Internal.asm)
    
    
    
    testImplementation("org.slf4j", "slf4j-api", Versions.Libraries.slf4j)
}
