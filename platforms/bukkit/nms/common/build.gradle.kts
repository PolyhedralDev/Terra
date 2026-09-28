plugins {
    id("io.papermc.paperweight.userdev")
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

dependencies {
    compileOnly(project(":platforms:bukkit:common"))
    compileOnly("xyz.jpenilla", "reflection-remapper", Versions.Bukkit.reflectionRemapper)
    paperweight.paperDevBundle(Versions.Bukkit.paperDevBundle)
}

tasks.withType<Jar>().configureEach {
    archiveBaseName.set("terra-bukkit-nms-common")
}
