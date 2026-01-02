plugins {
    id("io.papermc.paperweight.userdev")
    id("xyz.jpenilla.run-paper") version Versions.Bukkit.runPaper
}

dependencies {
    // Required for :platforms:bukkit:runDevBundleServer task
    paperweight.paperDevBundle(Versions.Bukkit.paperDevBundle)

    shaded(project(":platforms:bukkit:common"))
    shaded(project(":platforms:bukkit:nms"))
    shaded("xyz.jpenilla", "reflection-remapper", Versions.Bukkit.reflectionRemapper)
}

tasks {
<<<<<<< HEAD
    shadowJar {
=======
    // paperweight-userdev automatically uses shadowJar as the input for reobfJar when shadow is applied.
    assemble {
        dependsOn(reobfJar)
    }

java {
        withSourcesJar()
    }

shadowJar {
>>>>>>> 86e1828d0 (Initial fork: Terra 7.0.3 (lootfix + 1.21.10 compat + Java 21-25))
        relocate("io.papermc.lib", "com.dfsek.terra.lib.paperlib")
        relocate("com.google.common", "com.dfsek.terra.lib.google.common")
        relocate("org.apache.logging.slf4j", "com.dfsek.terra.lib.slf4j-over-log4j")
        exclude("org/slf4j/**")
        exclude("org/checkerframework/**")
        exclude("org/jetbrains/annotations/**")
        exclude("org/intellij/**")
        exclude("com/google/errorprone/**")
        exclude("com/google/j2objc/**")
        exclude("javax/**")
    }

<<<<<<< HEAD
=======
    shadowJar {
        finalizedBy(named("reobfJar"))
    }

    build {
        dependsOn(named("reobfJar"))
    }

>>>>>>> 86e1828d0 (Initial fork: Terra 7.0.3 (lootfix + 1.21.10 compat + Java 21-25))
    runServer {
        minecraftVersion(Versions.Bukkit.minecraft)
        dependsOn(shadowJar)
        pluginJars(shadowJar.get().archiveFile)

        downloadPlugins {
            modrinth("viaversion", "5.5.0")
            modrinth("viabackwards", "5.5.0")
        }
    }
}


addonDir(project.file("./run/plugins/Terra/addons"), tasks.named("runServer").get())
