repositories {
    mavenLocal()
}

dependencies {
    shadedApi(project(":common:implementation:base"))

    compileOnly("org.projectlombok:lombok:1.18.32")
    compileOnly("org.allaymc", "Allay-API", "1.0.0")

    annotationProcessor("org.projectlombok:lombok:1.18.32")
}