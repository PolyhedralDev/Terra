repositories {
    maven("https://www.jitpack.io/")
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    implementation("com.google.code.gson", "gson", "2.11.0")

    compileOnly("org.projectlombok:lombok:1.18.32")
    compileOnly(group = "org.allaymc.allay", name = "Allay-API", version = "master-SNAPSHOT")

    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

tasks {
    compileJava {
        options.release.set(21)
    }
}
