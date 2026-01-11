version = version("0.1.0")

dependencies {
}

tasks.withType<Jar> {
    manifest {
        attributes("Terra-Bootstrap-Addon-Entry-Point" to "com.dfsek.terra.addon.loader.ApiAddonLoader")
    }
}

project.extra.set("bootstrap", true)