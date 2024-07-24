repositories {
    mavenLocal()
    maven("https://www.jitpack.io/")
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    implementation("com.google.code.gson", "gson", "2.11.0")

    compileOnly("org.projectlombok:lombok:1.18.32")
    compileOnly("org.allaymc", "Allay-API", "1.0.0")

    annotationProcessor("org.projectlombok:lombok:1.18.32")
}