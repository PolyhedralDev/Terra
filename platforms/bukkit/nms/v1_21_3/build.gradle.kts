apply(plugin = "io.papermc.paperweight.userdev")

dependencies {
    api(project(":platforms:bukkit:common"))
    paperDevBundle(Versions.Bukkit.paperDevBundle)
    implementation("xyz.jpenilla", "reflection-remapper", Versions.Bukkit.reflectionRemapper)
}

tasks {
    assemble {
        dependsOn("reobfJar")
    }
}