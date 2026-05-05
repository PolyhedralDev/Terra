plugins {
    id("io.papermc.paperweight.userdev")
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

dependencies {
    api(project(":platforms:bukkit:common"))
    paperweight.paperDevBundle(Versions.Bukkit.paperDevBundle)
    implementation("xyz.jpenilla", "reflection-remapper", Versions.Bukkit.reflectionRemapper)
}