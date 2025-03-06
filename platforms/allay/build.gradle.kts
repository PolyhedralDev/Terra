dependencies {
    shadedApi(project(":common:implementation:base"))

    implementation("com.google.code.gson", "gson", Versions.Allay.gson)

    compileOnly("org.allaymc.allay", "api", Versions.Allay.api)
}