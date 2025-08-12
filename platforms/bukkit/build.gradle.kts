plugins {
    id("io.papermc.paperweight.userdev")
    id("xyz.jpenilla.run-paper") version Versions.Bukkit.runPaper
}

dependencies {
    // Required for :platforms:bukkit:runDevBundleServer task
    paperweight.paperDevBundle(Versions.Bukkit.paperDevBundle)

    shaded(project(":platforms:bukkit:common"))
    shaded(project(":platforms:bukkit:nms:v1_21_8"))
    shaded("xyz.jpenilla", "reflection-remapper", Versions.Bukkit.reflectionRemapper)
}

tasks {
    shadowJar {
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

    runServer {
        minecraftVersion(Versions.Bukkit.minecraft)
        dependsOn(shadowJar)
        pluginJars(shadowJar.get().archiveFile)

        downloadPlugins {
            modrinth("viaversion", "5.3.2")
            modrinth("viabackwards", "5.3.2")
        }
    }
}


addonDir(project.file("./run/plugins/Terra/addons"), tasks.named("runServer").get())
