//import java.util.zip.ZipFile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.ByteArrayOutputStream
import java.net.URL


plugins {
    java
    maven
    idea
    id("com.github.johnrengelman.shadow").version("6.1.0")
}

val versionObj = Version("2", "2", "0", true)

allprojects {
    version = versionObj
    group = "com.dfsek.terra"

}

repositories {
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("http://maven.enginehub.org/repo/") }
    maven { url = uri("https://repo.codemc.org/repository/maven-public") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://maven.fabricmc.net/") }
    gradlePluginPortal()
    jcenter()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("org.apache.commons:commons-rng-core:1.3")
    implementation("net.jafama:jafama:2.3.2")


    implementation("commons-io:commons-io:2.4")
    implementation("org.apache.commons:commons-imaging:1.0-alpha2")

    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.0-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:1.7")

    compileOnly("com.googlecode.json-simple:json-simple:1.1")

    implementation("com.scireum:parsii:1.2.1")

    implementation("net.jafama:jafama:2.3.2")

    implementation("com.dfsek:Tectonic:1.0.3")




    // JUnit.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}



val compileJava: JavaCompile by tasks
val mainSourceSet: SourceSet = sourceSets["main"]

tasks.withType<ProcessResources> {
    include("**/*.yml")
    filter<org.apache.tools.ant.filters.ReplaceTokens>(
            "tokens" to mapOf(
                    "VERSION" to project.version.toString()
            )
    )
}

compileJava.apply {
    options.encoding = "UTF-8"
    doFirst {
        options.compilerArgs = mutableListOf("-Xlint:all")
    }
}

tasks.test {
    useJUnitPlatform()

    maxHeapSize = "4G"
    ignoreFailures = false
    failFast = true
    maxParallelForks = 12
}

tasks.named<ShadowJar>("shadowJar") {
    // Tell shadow to download the packs
    dependsOn(downloadDefaultPacks)

    archiveClassifier.set("")
    archiveBaseName.set("Terra")
    setVersion(project.version)
    relocate("org.apache.commons", "com.dfsek.terra.lib.commons")
    relocate("org.bstats.bukkit", "com.dfsek.terra.lib.bstats")
    relocate("parsii", "com.dfsek.terra.lib.parsii")
    relocate("io.papermc.lib", "com.dfsek.terra.lib.paperlib")
    relocate("net.jafama", "com.dfsek.terra.lib.jafama")
    relocate("com.dfsek.tectonic", "com.dfsek.terra.lib.tectonic")
    relocate("net.jafama", "com.dfsek.terra.lib.jafama")
    minimize()
}

tasks.build {
    dependsOn(tasks.shadowJar)
//    dependsOn(testWithPaper)
//    testWithPaper.mustRunAfter(tasks.shadowJar)
}



val downloadDefaultPacks = tasks.create("downloadDefaultPacks") {
    doFirst {
        // Downloading latest paper jar.
//        if (file("${buildDir}/resources/main/packs/default").exists() && file("${buildDir}/resources/main/packs/nether").exists())
//            return@doFirst
//        else
        file("${buildDir}/resources/main/packs/").deleteRecursively()

        val defaultPackUrl = URL("https://github.com/PolyhedralDev/TerraDefaultConfig/releases/download/latest/default.zip")
        downloadPack(defaultPackUrl)
        val netherPackUrl = URL("https://github.com/PolyhedralDev/TerraDefaultConfig/releases/download/latest/nether.zip")
        downloadPack(netherPackUrl)
    }
}



/**
 * Version class that does version stuff.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Version(val major: String, val minor: String, val revision: String, val preRelease: Boolean = false) {

    override fun toString(): String {
        return if (!preRelease)
            "$major.$minor.$revision"
        else //Only use git hash if it's a prerelease.
            "$major.$minor.$revision-BETA+${getGitHash()}"
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

fun gitClone(name: String) {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = mutableListOf("git", "clone", name)
        standardOutput = stdout
    }
}

fun downloadPack(packUrl: URL) {
    val fileName = packUrl.file.substring(packUrl.file.lastIndexOf("/"))
    val file = file("${buildDir}/resources/main/packs/${fileName}")
    file.parentFile.mkdirs()
    file.outputStream().write(packUrl.readBytes())
}