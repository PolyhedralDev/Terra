plugins {
    `java-library`
}

subprojects {
    group = "com.dfsek.terra.bukkit.nms"
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

dependencies {
    api(project(path = ":platforms:bukkit:nms:common", configuration = "runtimeElements"))
    api(project(path = ":platforms:bukkit:nms:26.1", configuration = "runtimeElements"))
    api(project(path = ":platforms:bukkit:nms:26.3", configuration = "runtimeElements"))
}
