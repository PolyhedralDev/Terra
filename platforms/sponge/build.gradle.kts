repositories {
    maven {
        url = uri("https://repo-new.spongepowered.org/repository/maven-public/")
    }
}

dependencies {
    "shadedApi"(project(":common:implementation"))
    annotationProcessor(implementation("org.spongepowered:spongeapi:8.0.0-SNAPSHOT")!!)
}
