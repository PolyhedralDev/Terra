version = version("1.0.0")

dependencies {
    api(libs.libraries.internal.apache.io)
    implementation(libs.libraries.tectonic.yaml)
}

tasks.withType<Jar> {
    manifest {
        attributes("Terra-Bootstrap-Addon-Entry-Point" to "com.dfsek.terra.addons.manifest.impl.ManifestAddonLoader")
    }
}

project.extra.set("bootstrap", true)