dependencies {
    "shadedApi"("commons-io:commons-io:2.6")
    "shadedImplementation"("com.dfsek.tectonic:yaml:2.1.2")
}

tasks.withType<Jar>() {
    manifest {
        attributes("Bootstrap-Addon-Entry-Point" to "com.dfsek.terra.addons.manifest.impl.ManifestAddonLoader")
    }
}