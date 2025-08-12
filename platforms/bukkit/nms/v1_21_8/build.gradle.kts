plugins {
    id("io.papermc.paperweight.userdev")
}

dependencies {
    api(project(":platforms:bukkit:common"))
    paperweight.paperDevBundle(Versions.Bukkit.paperDevBundle)
    implementation("xyz.jpenilla", "reflection-remapper", Versions.Bukkit.reflectionRemapper)
}