repositories {
    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
    maven("https://jitpack.io/")
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    implementation("com.google.code.gson", "gson", "2.11.0")

    compileOnly("org.projectlombok:lombok:1.18.32")
    compileOnly(group = "org.allaymc.allay", name = "api", version = "master-SNAPSHOT")

    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

tasks {
    compileJava {
        options.release.set(21)
    }
}
