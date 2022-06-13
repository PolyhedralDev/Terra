apply(plugin = "io.papermc.paperweight.userdev")

repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    api(project(":platforms:bukkit:common"))
    paperDevBundle("1.19-R0.1-SNAPSHOT")
    implementation("xyz.jpenilla", "reflection-remapper", "0.1.0-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn("reobfJar")
    }
}