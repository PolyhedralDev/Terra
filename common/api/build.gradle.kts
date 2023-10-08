dependencies {
    api("ca.solo-studios", "strata", Versions.Libraries.strata)
    compileOnlyApi("org.slf4j", "slf4j-api", Versions.Libraries.slf4j)
    testImplementation("org.slf4j", "slf4j-api", Versions.Libraries.slf4j)
    api("cloud.commandframework", "cloud-core", Versions.Libraries.cloud)
    
    api("com.dfsek.tectonic", "common", Versions.Libraries.tectonic)
    
    api("com.github.ben-manes.caffeine:caffeine:3.1.0")

    api("io.github.aecsocket", "cpu-features-jni", Versions.Libraries.Internal.cpuFeaturesJava)
    api("io.github.aecsocket", "cpu-features-jni-natives-linux", Versions.Libraries.Internal.cpuFeaturesJava)
    api("io.github.aecsocket", "cpu-features-jni-natives-windows", Versions.Libraries.Internal.cpuFeaturesJava)
    api("io.github.aecsocket", "cpu-features-jni-natives-macos", Versions.Libraries.Internal.cpuFeaturesJava)
}