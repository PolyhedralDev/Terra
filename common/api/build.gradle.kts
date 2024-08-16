dependencies {
    api("ca.solo-studios", "strata", Versions.Libraries.strata)
    compileOnlyApi("org.slf4j", "slf4j-api", Versions.Libraries.slf4j)
    testImplementation("org.slf4j", "slf4j-api", Versions.Libraries.slf4j)
    api("org.incendo", "cloud-core", Versions.Libraries.cloud)

    api("com.dfsek.tectonic", "common", Versions.Libraries.tectonic)

    api("com.github.ben-manes.caffeine", "caffeine", Versions.Libraries.caffeine)

}