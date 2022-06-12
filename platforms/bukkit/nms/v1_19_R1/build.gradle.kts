plugins {
    id("io.papermc.paperweight.userdev") version "1.3.3"
}

dependencies {
    api(project(":platforms:bukkit:common"))
    paperDevBundle("1.19-R0.1-SNAPSHOT")
    
    compileOnly(group = "org.spigotmc", name = "spigot", version = "1.18.2-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}