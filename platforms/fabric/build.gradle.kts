plugins {
    java
    maven
    idea
}

group = "com.dfsek.terra.bukkit"

repositories {
    mavenCentral()
    maven { url = uri("http://maven.enginehub.org/repo/") }
    maven { url = uri("https://repo.codemc.org/repository/maven-public") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    implementation(project(":common"))
    implementation("org.apache.commons:commons-rng-core:1.3")


    implementation("com.scireum:parsii:1.2.1")
    implementation("com.dfsek:Tectonic:1.0.3")
    implementation("net.jafama:jafama:2.3.2")

    compileOnly("com.googlecode.json-simple:json-simple:1.1")

    implementation("com.google.guava:guava:30.0-jre")

    compileOnly("org.jetbrains:annotations:20.1.0")
}
