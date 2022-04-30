version = version("0.1.0")

dependencies {
    api("commons-io:commons-io:2.6")
    implementation("com.dfsek.tectonic:yaml:${Versions.Libraries.tectonic}")
}

tasks.withType<Jar> {
    manifest {
        attributes("Terra-Bootstrap-Addon-Entry-Point" to "com.dfsek.terra.addons.manifest.impl.ManifestAddonLoader")
    }
}

project.extra.set("bootstrap", true)