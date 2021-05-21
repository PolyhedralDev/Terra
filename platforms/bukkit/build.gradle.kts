import com.dfsek.terra.configureCommon
import com.dfsek.terra.gitClone
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

plugins {
    `java-library`
    `maven-publish`
}
configureCommon()

group = "com.dfsek.terra.bukkit"

val mcVersion = "1.16.5"
val testDir = "target/server"
val testMem = "3G"

val paperURL = "https://papermc.io/api/v1/paper/%version%/latest/download/"
val purpurURL = "https://ci.pl3x.net/job/Purpur/lastSuccessfulBuild/artifact/final/purpurclip.jar"

dependencies {
    "shadedApi"(project(":common"))

    "compileOnly"("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    "shadedImplementation"("io.papermc:paperlib:1.0.5")

    "shadedImplementation"("org.bstats:bstats-bukkit:1.7")

    "compileOnly"("com.sk89q.worldedit:worldedit-bukkit:7.2.0-SNAPSHOT")

    "shadedApi"("com.google.guava:guava:30.0-jre")
}

val jvmFlags = listOf("-XX:+UseG1GC", "-XX:+ParallelRefProcEnabled", "-XX:MaxGCPauseMillis=200",
        "-XX:+UnlockExperimentalVMOptions", "-XX:+DisableExplicitGC", "-XX:+AlwaysPreTouch",
        "-XX:G1NewSizePercent=30", "-XX:G1MaxNewSizePercent=40", "-XX:G1HeapRegionSize=8M",
        "-XX:G1ReservePercent=20", "-XX:G1HeapWastePercent=5", "-XX:G1MixedGCCountTarget=4",
        "-XX:InitiatingHeapOccupancyPercent=15", "-XX:G1MixedGCLiveThresholdPercent=90",
        "-XX:G1RSetUpdatingPauseTimePercent=5", "-XX:SurvivorRatio=32", "-XX:+PerfDisableSharedMem",
        "-XX:MaxTenuringThreshold=1", "-Dusing.aikars.flags=https://mcflags.emc.gs",
        "-Daikars.new.flags=true", "-DIReallyKnowWhatIAmDoingISwear", "-javaagent:paperclip.jar")

fun downloadPaperclip(url: String, dir: String) {
    val clip = URL(url.replace("%version%", mcVersion))
    val clipReadableByteChannel = Channels.newChannel(clip.openStream())
    val clipFile = file("$testDir/$dir/paperclip.jar")
    val clipOutputStream = clipFile.outputStream()
    val clipFileChannel = clipOutputStream.channel
    clipFileChannel.transferFrom(clipReadableByteChannel, 0, Long.MAX_VALUE)
}

fun copyTerra(dir: String) {
    file("$testDir/$dir").walk().forEach {
        if(it.name.startsWith("Terra-") && it.name.endsWith(".jar")) it.delete() // Delete old Terra jar(s)
    }
    copy {
        from("$buildDir/libs/Terra-bukkit-$version-shaded.jar")
        into("$testDir/$dir/plugins/")
    }
}

fun installServer(dir: String) {
    // clean
    file("$testDir/$dir").deleteRecursively()
    file("$testDir/$dir/plugins").mkdirs()
    // Cloning test setup.
    gitClone("https://github.com/PolyhedralDev/WorldGenTestServer")
    // Copying plugins
    Files.move(file("WorldGenTestServer/plugins").toPath(),
            file("$testDir/$dir/plugins").toPath(),
            StandardCopyOption.REPLACE_EXISTING)
    // Copying config
    val serverText = URL("https://raw.githubusercontent.com/PolyhedralDev/WorldGenTestServer/master/server.properties").readText()
    file("$testDir/$dir/server.properties").writeText(serverText)
    val bukkitText = URL("https://raw.githubusercontent.com/PolyhedralDev/WorldGenTestServer/master/bukkit.yml").readText()
    file("$testDir/$dir/bukkit.yml").writeText(bukkitText.replace("\${world}", "world").replace("\${gen}", "Terra:DEFAULT"))

    println("By proceeding, you are agreeing to the Minecraft EULA: https://account.mojang.com/documents/minecraft_eula")
    file("$testDir/$dir/eula.txt").writeText("eula=true")

    // clean up
    file("WorldGenTestServer").deleteRecursively()
}

fun deleteFolder(folder: File) {
    if(folder.exists()) folder.deleteRecursively()
}

fun deleteFile(file: File) {
    if(file.exists()) file.delete()
}

tasks.create("cleanWorlds") {
    group = "bukkit"
    doFirst {
        deleteFolder(file("$testDir/paper/world"))
        deleteFolder(file("$testDir/paper/world_nether"))
        deleteFolder(file("$testDir/paper/world_the_end"))

        deleteFolder(file("$testDir/purpur/world"))
        deleteFolder(file("$testDir/purpur/world_nether"))
        deleteFolder(file("$testDir/purpur/world_the_end"))
    }
}

tasks.create("updatePaper") {
    group = "bukkit"
    doFirst {
        deleteFile(file("$testDir/paper/paperclip.jar"))
        downloadPaperclip(paperURL, "paper")
    }
}

tasks.create("updatePurpur") {
    group = "bukkit"
    doFirst {
        deleteFile(file("$testDir/paper/paperclip.jar"))
        downloadPaperclip(purpurURL, "purpur")
    }
}

tasks.create("installPaper") {
    group = "bukkit"
    dependsOn("shadowJar")
    doFirst {
        installServer("paper")
        // Downloading latest paper jar.
        downloadPaperclip(paperURL, "paper")
    }
}

tasks.create("installPurpur") {
    group = "bukkit"
    dependsOn("shadowJar")
    doFirst {
        installServer("purpur")
        // Downloading latest paper jar.
        downloadPaperclip(purpurURL, "purpur")
    }
}

task<JavaExec>(name = "runPaper") {
    group = "bukkit"
    standardInput = System.`in`
    dependsOn("shadowJar")
    // Copy Terra into dir
    doFirst {
        copyTerra("paper")
    }

    main = "io.papermc.paperclip.Paperclip"
    jvmArgs = jvmFlags
    maxHeapSize = testMem
    minHeapSize = testMem
    //args = listOf("nogui")
    workingDir = file("$testDir/paper")
    classpath = files("$testDir/paper/paperclip.jar")
}

task<JavaExec>(name = "runPurpur") {
    group = "bukkit"
    standardInput = System.`in`
    dependsOn("shadowJar")
    // Copy Terra into dir
    doFirst {
        copyTerra("purpur")
    }

    main = "io.papermc.paperclip.Paperclip"
    jvmArgs = jvmFlags
    maxHeapSize = testMem
    minHeapSize = testMem
    //args = listOf("nogui")
    workingDir = file("$testDir/purpur")
    classpath = files("$testDir/purpur/paperclip.jar")
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.bstats.bukkit", "com.dfsek.terra.lib.bstats")
    relocate("io.papermc.lib", "com.dfsek.terra.lib.paperlib")
    relocate("com.google.common", "com.dfsek.terra.lib.google.common")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks["sourcesJar"])
            artifact(tasks["jar"])
        }
    }

    repositories {
        val mavenUrl = "https://repo.codemc.io/repository/maven-releases/"

        maven(mavenUrl) {
            val mavenUsername: String? by project
            val mavenPassword: String? by project
            if (mavenUsername != null && mavenPassword != null) {
                credentials {
                    username = mavenUsername
                    password = mavenPassword
                }
            }
        }
    }
}