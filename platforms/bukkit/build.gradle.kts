plugins {
    id("io.papermc.paperweight.userdev")
    id("xyz.jpenilla.run-paper") version Versions.Bukkit.runPaper
}

group = "org.nullnomads"

tasks.withType<org.gradle.jvm.tasks.Jar>().configureEach {
    archiveBaseName.set("NullNomadsWorldgen")
}

extensions.configure<org.gradle.api.publish.PublishingExtension> {
    publications.withType<org.gradle.api.publish.maven.MavenPublication>().configureEach {
        artifactId = "NullNomadsWorldgen"
    }
}

dependencies {
    // Required for :platforms:bukkit:runDevBundleServer task
    paperweight.paperDevBundle(Versions.Bukkit.paperDevBundle)

    shaded(project(":platforms:bukkit:common"))
    shaded(project(":platforms:bukkit:nms"))
    shaded("xyz.jpenilla", "reflection-remapper", Versions.Bukkit.reflectionRemapper)
}

tasks {
    shadowJar {
        relocate("io.papermc.lib", "com.dfsek.terra.lib.paperlib")
        relocate("net.kyori.adventure.nbt", "org.nullnomads.worldgen.lib.adventure.nbt")
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
            modrinth("viaversion", "5.5.0")
            modrinth("viabackwards", "5.5.0")
        }
    }

    val cleanPaperTestServer = register<Delete>("cleanPaperTestServer") {
        delete(layout.projectDirectory.dir("run-clean"))
    }

    val prepareCleanPaperTestServer = register("prepareCleanPaperTestServer") {
        dependsOn(cleanPaperTestServer)
        doLast {
            val runDirectory = layout.projectDirectory.dir("run-clean").asFile
            runDirectory.mkdirs()
            runDirectory.resolve("eula.txt").writeText("eula=true\n")
        }
    }

    register<xyz.jpenilla.runpaper.task.RunServer>("runCleanServer") {
        group = "application"
        description = "Runs a fresh Paper 1.21.11 server with only NullNomadsWorldgen installed."
        minecraftVersion(Versions.Bukkit.minecraft)
        runDirectory(project.file("./run-clean"))
        dependsOn(prepareCleanPaperTestServer, shadowJar)
        pluginJars(shadowJar.get().archiveFile)
    }
}


addonDir(project.file("./run/plugins/NullNomadsWorldgen/addons"), tasks.named("runServer").get())
