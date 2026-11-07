plugins {
    id("io.papermc.paperweight.userdev")
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

dependencies {
    compileOnly(project(":platforms:bukkit:common"))
    compileOnly(project(":platforms:bukkit:nms:common"))
    paperweight.paperDevBundle(Versions.Bukkit.paperDevBundle26_3)
}
