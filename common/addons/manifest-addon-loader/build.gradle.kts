import com.dfsek.terra.version

version = version("0.1.0")

dependencies {
    shadedApi("commons-io:commons-io:2.6")
    shadedImplementation("com.dfsek.tectonic:yaml:3.1.0")
}

tasks.withType<Jar> {
    manifest {
        attributes("Terra-Bootstrap-Addon-Entry-Point" to "com.dfsek.terra.addons.manifest.impl.ManifestAddonLoader")
    }
}

project.extra.set("bootstrap", true)