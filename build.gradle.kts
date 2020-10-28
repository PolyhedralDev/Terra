import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.ByteArrayOutputStream

buildDir = file("target")

plugins {
    java
    id("com.github.johnrengelman.shadow").version("6.1.0")
}

repositories {
    flatDir {
        dirs("lib")
    }
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        url = uri("http://maven.enginehub.org/repo/")
    }
    maven {
        url = uri("https://repo.codemc.org/repository/maven-public")
    }
    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
}

base {
    libsDirName = "prod"
}

java {

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val versionObj = Version("0", "0", "1", "dev.1")
version = versionObj

dependencies {
    implementation("org.jetbrains:annotations:20.1.0") // more recent.
    implementation("commons-io:commons-io:2.4")
    implementation(name = "Gaea-1.12.2", group = "")
    implementation("org.apache.commons:commons-imaging:1.0-alpha2")
    implementation("com.sk89q.worldedit:worldedit-bukkit:7.2.0-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:1.7")
    implementation("com.googlecode.json-simple:json-simple:1.1")
    implementation(name = "parsii-1.2", group = "")
    implementation("io.papermc:paperlib:1.0.5")
    
    // JUnit.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks.test {
    useJUnitPlatform()

    maxHeapSize = "1G"
    ignoreFailures = false
    failFast = true
    maxParallelForks = 12
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    archiveBaseName.set("Terra")
    setVersion(project.version)
    dependencies {
        include(dependency("commons-io:commons-io"))
        include(dependency("org.apache.commons:commons-imaging"))
        include(dependency("org.bstats:bstats-bukkit"))
        include(dependency(":parsii-1.2"))
        include(dependency("io.papermc:paperlib"))
    }
    relocate("org.apache.commons", "com.dfsek.terra.lib.commons")
    relocate("org.bstats.bukkit", "com.dfsek.terra.lib.bstats")
    relocate("parsii", "com.dfsek.terra.lib.parsii")
    relocate("io.papermc.lib", "com.dfsek.terra.lib.paperlib")
}

tasks.build {
    dependsOn(tasks.test)
    dependsOn("shadowJar")
}


/**
 * Version class that does version stuff.
 */
class Version(val major: String, val minor: String, val revision: String, val preReleaseData: String? = null) {
    
    override fun toString(): String {
        return if (preReleaseData.isNullOrBlank())
            "$major.$minor.$revision"
        else //Only use git hash if it's a prerelease.
            "$major.$minor.$revision-$preReleaseData+${getGitHash()}"
    }
}

fun getGitHash(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = mutableListOf("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}
