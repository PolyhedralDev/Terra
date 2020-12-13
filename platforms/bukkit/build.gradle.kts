import com.dfsek.terra.configureCommon
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.ByteArrayOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

plugins {
    `java-library`
}
configureCommon()

group = "com.dfsek.terra.bukkit"

repositories {
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("http://maven.enginehub.org/repo/") }
    maven { url = uri("https://repo.codemc.org/repository/maven-public") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    implementation(project(":common"))

    compileOnly("org.spigotmc:spigot-api:1.16.2-R0.1-SNAPSHOT")
    implementation("io.papermc:paperlib:1.0.5")

    implementation("org.bstats:bstats-bukkit:1.7")

    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.0-SNAPSHOT")
}

tasks.withType<ProcessResources> {
    include("**/*.yml")
    filter<org.apache.tools.ant.filters.ReplaceTokens>(
            "tokens" to mapOf(
                    "VERSION" to project.version.toString()
            )
    )
}

val testDir = "target/server/"

val setupServer = tasks.create("setupServer") {
    dependsOn("shadowJar")
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

val testWithPaper = task<JavaExec>(name = "testWithPaper") {
    standardInput = System.`in`
    dependsOn("shadowJar")
    // Copy Terra into dir
    doFirst {
        copy {
            from("${buildDir}/libs/Terra-${version}.jar")
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
    relocate("org.bstats.bukkit", "com.dfsek.terra.lib.bstats")
    relocate("io.papermc.lib", "com.dfsek.terra.lib.paperlib")
}

fun gitClone(name: String) {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = mutableListOf("git", "clone", name)
        standardOutput = stdout
    }
}
