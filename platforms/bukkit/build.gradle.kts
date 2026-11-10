plugins {
    id("xyz.jpenilla.run-paper") version Versions.Bukkit.runPaper
}

dependencies {
    shaded(project(":platforms:bukkit:common"))
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

tasks {
    shadowJar {
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
        // Modrinth's Wyck lacks our fixes.
        val wyckJar = file("${System.getProperty("user.home")}/Wyck/paper/build/libs/Wyck-${Versions.Bukkit.wyck}.jar")
        pluginJars(wyckJar)
        doFirst {
            if(!wyckJar.isFile) {
                throw GradleException("No Wyck plugin jar at $wyckJar: build and publish ~/Wyck (Wyck ${Versions.Bukkit.wyck}) first")
            }
        }

        downloadPlugins {
            modrinth("viaversion", "5.12.2")
            modrinth("viabackwards", "5.12.2")
        }
    }
}


addonDir(project.file("./run/plugins/Terra/addons"), tasks.named("runServer").get())
