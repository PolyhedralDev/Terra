import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.ByteArrayOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
//import java.util.zip.ZipFile
import java.util.zip.ZipInputStream

plugins {
    java
    maven
    id("com.github.johnrengelman.shadow").version("6.1.0")
}

repositories {
    mavenCentral()
    flatDir {
        dirs("lib")
    }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("http://maven.enginehub.org/repo/") }
    maven { url = uri("https://repo.codemc.org/repository/maven-public") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val versionObj = Version("1", "3", "1", true)

version = versionObj

dependencies {

    // Commons. Used for stuff.
    implementation("commons-io:commons-io:2.4")
    // Commons-imagine. Used for loading images from disk for the biome images.
    implementation("org.apache.commons:commons-imaging:1.0-alpha2")
    // Bstats. For tracking stats.
    implementation("org.bstats:bstats-bukkit:1.7")
    // Parsii. Does parsing of the equations.
    implementation(name = "parsii-1.2", group = "")
    // Papermc API. for Paper spigot
    implementation("io.papermc:paperlib:1.0.5")

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-core:2.12+")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.12+")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12+")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12+")

    // Spigot mc API. Provided by spigot.
    compileOnly("org.spigotmc:spigot-api:1.16.2-R0.1-SNAPSHOT")
    // Annotations. Provided by mc.
    compileOnly("org.jetbrains:annotations:20.1.0")
    // Gaea. Provided as a plugin.
    val gaeaVersion = "1.14.2"
    compileOnly(name = "Gaea-${gaeaVersion}", group = "")
    testImplementation(name = "Gaea-${gaeaVersion}", group = "")
    // Worldedit. For saving structures. Provided as a plugin.
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.0-SNAPSHOT")
    // GSON. idk how it's used.
    compileOnly("com.googlecode.json-simple:json-simple:1.1")

    // JUnit. Used for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")

    // Included here becaues tests rely on it.
    testImplementation(name = "Gaea-1.14.2", group = "")
    testImplementation("org.spigotmc:spigot-api:1.16.2-R0.1-SNAPSHOT")
}

val compileJava: JavaCompile by tasks
val mainSourceSet: SourceSet = sourceSets["main"]

val tokenizeJavaSources = task<Copy>(name = "tokenizeJavaSources") {
    from(mainSourceSet.allSource) {
        include("**/plugin.yml")
        val tokens = mapOf("VERSION" to versionObj.toString())

        filter(org.apache.tools.ant.filters.ReplaceTokens::class, "tokens" to tokens)
    }
    into("build/tokenizedSources")
    includeEmptyDirs = false
}


compileJava.apply {
    dependsOn(tokenizeJavaSources)

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
    from(tokenizeJavaSources.destinationDir)

    archiveClassifier.set("")
    archiveBaseName.set("Terra")
    setVersion(project.version)
    relocate("org.apache.commons", "com.dfsek.terra.lib.commons")
    relocate("org.bstats.bukkit", "com.dfsek.terra.lib.bstats")
    relocate("parsii", "com.dfsek.terra.lib.parsii")
    relocate("io.papermc.lib", "com.dfsek.terra.lib.paperlib")
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
        downloadAndUnzipPack(defaultPackUrl)
        val netherPackUrl = URL("https://github.com/PolyhedralDev/TerraDefaultConfig/releases/download/latest/nether.zip")
        downloadAndUnzipPack(netherPackUrl)
    }
}
tasks.processResources.get().dependsOn(downloadDefaultPacks)

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

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    archiveBaseName.set("Terra")
    setVersion(project.version)
    relocate("org.apache.commons", "com.dfsek.terra.lib.commons")
    relocate("org.bstats.bukkit", "com.dfsek.terra.lib.bstats")
    relocate("parsii", "com.dfsek.terra.lib.parsii")
    relocate("io.papermc.lib", "com.dfsek.terra.lib.paperlib")
    relocate("com.fasterxml.jackson", "com.dfsek.terra.lib.jackson")
}

tasks.build {
    dependsOn(tasks.test)
    dependsOn(tasks.shadowJar)
//    dependsOn(testWithPaper)
    tasks.shadowJar.get().mustRunAfter(tasks.test)
//    testWithPaper.mustRunAfter(tasks.shadowJar)
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
    val exitCode = exec {
        commandLine = mutableListOf("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }.exitValue
    if (exitCode == 128) // https://canary.discord.com/channels/715448651786485780/765260067812540416/777236094768381954
        System.err.println("You can only use this in a git repo. Please run \"git clone https://github.com/PolyhedralDev/Terra.git\", then cd into there.")
    if (exitCode == 127 || exitCode == 9009) // https://canary.discord.com/channels/715448651786485780/715448652411437099/777304618853335082
        System.err.println("You must install git for this to work. https://git-scm.com/downloads")
    return stdout.toString().trim()
}

fun gitClone(name: String) {
    val stdout = ByteArrayOutputStream()
    val result = exec {
        commandLine = mutableListOf("git", "clone", name)
        standardOutput = stdout
    }.exitValue
    if (result == 127 || result == 9009) // https://canary.discord.com/channels/715448651786485780/715448652411437099/777304618853335082
        System.err.println("You must install git for this to work.")
}

fun downloadAndUnzipPack(packUrl: URL) {
    ZipInputStream(packUrl.openStream()).use { zip ->
        while (true) {
            val entry = zip.nextEntry ?: break
            if (entry.isDirectory)
                file("${buildDir}/resources/main/packs/${entry.name}").mkdirs()
            else
                file("${buildDir}/resources/main/packs/${entry.name}").outputStream().use { output ->
                    output.write(zip.readBytes())
                }
        }
    }
}