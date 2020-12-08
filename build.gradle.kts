//import java.util.zip.ZipFile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.ByteArrayOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

plugins {
    java
    maven
    id("com.github.johnrengelman.shadow").version("6.1.0")
}

repositories {
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("http://maven.enginehub.org/repo/") }
    maven { url = uri("https://repo.codemc.org/repository/maven-public") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val versionObj = Version("2", "0", "0", true)

version = versionObj

dependencies {
    val gaeaVersion = "1.15.0"
    compileOnly("org.polydev.gaea:Gaea:${gaeaVersion}")
    testImplementation("org.polydev.gaea:Gaea:${gaeaVersion}")

    compileOnly("org.jetbrains:annotations:20.1.0")

    implementation("commons-io:commons-io:2.4")
    implementation("org.apache.commons:commons-imaging:1.0-alpha2")

    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.0-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:1.7")

    compileOnly("com.googlecode.json-simple:json-simple:1.1")

    implementation("com.scireum:parsii:1.2.1")

    compileOnly("org.spigotmc:spigot-api:1.16.2-R0.1-SNAPSHOT")
    implementation("io.papermc:paperlib:1.0.5")

    implementation("net.jafama:jafama:2.3.2")

    implementation("com.dfsek:Tectonic:1.0.3")


    // JUnit.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

val compileJava: JavaCompile by tasks
val mainSourceSet: SourceSet = sourceSets["main"]

tasks.withType<ProcessResources> {
    include("*.yml")
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
    minimize()
}

tasks.build {
    dependsOn(tasks.shadowJar)
//    dependsOn(testWithPaper)
//    testWithPaper.mustRunAfter(tasks.shadowJar)
}

val testDir = "target/server/"

val setupServer = tasks.create("setupServer") {
    dependsOn(tasks.shadowJar)
    doFirst {
        // clean
        file("${testDir}/").deleteRecursively()
        file("${testDir}/plugins").mkdirs()

        // Downloading latest paper jar.
        val paperUrl = URL("https://papermc.io/api/v1/paper/1.16.4/latest/download")
        val paperReadableByteChannel = Channels.newChannel(paperUrl.openStream())
        val paperFile = file("${testDir}/paper.jar")
        val paperFileOutputStream = paperFile.outputStream()
        val paperFileChannel = paperFileOutputStream.channel
        paperFileChannel.transferFrom(paperReadableByteChannel, 0, Long.MAX_VALUE)

        // Cloning test setup.
        gitClone("https://github.com/PolyhedralDev/WorldGenTestServer")
        // Copying plugins
        Files.move(Paths.get("WorldGenTestServer/plugins"),
                Paths.get("$testDir/plugins"),
                StandardCopyOption.REPLACE_EXISTING)
        // Copying config
        val serverText = URL("https://raw.githubusercontent.com/PolyhedralDev/WorldGenTestServer/master/server.properties").readText()
        file("${testDir}/server.properties").writeText(serverText)
        val bukkitText = URL("https://raw.githubusercontent.com/PolyhedralDev/WorldGenTestServer/master/bukkit.yml").readText()
        file("${testDir}/bukkit.yml").writeText(bukkitText.replace("\${world}", "world").replace("\${gen}", "Terra:DEFAULT"))

        File("${testDir}/eula.txt").writeText("eula=true")

        // clean up
        file("WorldGenTestServer").deleteRecursively()
    }
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

val testWithPaper = task<JavaExec>(name = "testWithPaper") {
    standardInput = System.`in`
    dependsOn(tasks.shadowJar)
    // Copy Terra into dir
    doFirst {
        copy {
            from("${buildDir}/libs/Terra-${versionObj}.jar")
            into("${testDir}/plugins/")
        }
    }

    main = "io.papermc.paperclip.Paperclip"
    jvmArgs = listOf("-XX:+UseG1GC", "-XX:+ParallelRefProcEnabled", "-XX:MaxGCPauseMillis=200",
            "-XX:+UnlockExperimentalVMOptions", "-XX:+DisableExplicitGC", "-XX:+AlwaysPreTouch",
            "-XX:G1NewSizePercent=30", "-XX:G1MaxNewSizePercent=40", "-XX:G1HeapRegionSize=8M",
            "-XX:G1ReservePercent=20", "-XX:G1HeapWastePercent=5", "-XX:G1MixedGCCountTarget=4",
            "-XX:InitiatingHeapOccupancyPercent=15", "-XX:G1MixedGCLiveThresholdPercent=90",
            "-XX:G1RSetUpdatingPauseTimePercent=5", "-XX:SurvivorRatio=32", "-XX:+PerfDisableSharedMem",
            "-XX:MaxTenuringThreshold=1", "-Dusing.aikars.flags=https://mcflags.emc.gs",
            "-Daikars.new.flags=true", "-DIReallyKnowWhatIAmDoingISwear")
    maxHeapSize = "2G"
    args = listOf("nogui")
    workingDir = file("${testDir}/")
    classpath = files("${testDir}/paper.jar")
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