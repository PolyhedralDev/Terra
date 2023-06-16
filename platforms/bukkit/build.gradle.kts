plugins {
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    shaded(project(":platforms:bukkit:common"))
    shaded(project(":platforms:bukkit:nms:v1_18_R2", configuration = "reobf"))
    shaded(project(":platforms:bukkit:nms:v1_19_R1", configuration = "reobf"))
    shaded(project(":platforms:bukkit:nms:v1_19_R2", configuration = "reobf"))
    shaded(project(":platforms:bukkit:nms:v1_19_R3", configuration = "reobf"))
    shaded(project(":platforms:bukkit:nms:v1_20_R1", configuration = "reobf"))
    shaded("xyz.jpenilla", "reflection-remapper", Versions.Bukkit.reflectionRemapper)
}

tasks {
    shadowJar {
        relocate("org.bstats.bukkit", "com.dfsek.terra.lib.bstats")
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
    }
}


addonDir(project.file("./run/plugins/Terra/addons"), tasks.named("runServer").get())
