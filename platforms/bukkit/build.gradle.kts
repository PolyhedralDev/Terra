plugins {
    alias(libs.plugins.bukkit.run.paper)
}

repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    shaded(project(":platforms:bukkit:common"))
    shaded(project(":platforms:bukkit:nms:v1_19_R1", configuration = "reobf"))
    shaded(libs.bukkit.reflection.remapper)
}

tasks {
    shadowJar {
        relocate("io.papermc.lib", "com.dfsek.terra.lib.paperlib")
        exclude("org/slf4j/**")
        exclude("org/checkerframework/**")
        exclude("org/jetbrains/annotations/**")
        exclude("org/intellij/**")
        exclude("com/google/errorprone/**")
        exclude("com/google/j2objc/**")
        exclude("javax/**")
    }
    
    runServer {
        minecraftVersion(libs.versions.bukkit.minecraft.get())
        dependsOn(shadowJar)
        pluginJars(shadowJar.get().archiveFile)
    }
}


addonDir(project.file("./target/server/paper/plugins/Terra/addons"), tasks.named("runServer").get())
