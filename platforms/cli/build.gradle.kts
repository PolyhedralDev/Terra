repositories {
    maven { url = uri("https://jitpack.io/") }
}

dependencies {
    shadedApi("commons-io:commons-io:2.6")
    shadedApi("com.github.Querz:NBT:6.1")
    shadedApi(project(":common:implementation:base"))
    
    implementation("com.google.guava:guava:31.0.1-jre")
    
    implementation("ch.qos.logback:logback-classic:1.2.7")
}

tasks.named("build") {
    finalizedBy(tasks.named("shadowJar"))
}