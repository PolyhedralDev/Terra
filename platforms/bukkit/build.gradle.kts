import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.Files
import java.nio.file.StandardCopyOption


val mcVersion = "1.18.2"
val testDir = "target/server"
val testMem = "3G"

val paperBuild = 350
val paperURL = "https://papermc.io/api/v2/projects/paper/versions/%version%/builds/$paperBuild/downloads/paper-%version%-$paperBuild.jar"
val purpurURL = "https://api.purpurmc.org/v2/purpur/%version%/latest/download"

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    api("org.slf4j:slf4j-api:1.8.0-beta4") {
        because("Minecraft 1.17+ includes slf4j 1.8.0-beta4, so we need to shade it for other versions.")
    }
    implementation("org.apache.logging.log4j", "log4j-slf4j18-impl", Versions.Libraries.log4j_slf4j_impl) {
        because("Minecraft 1.17+ includes slf4j 1.8.0-beta4, so we need to shade it for other versions.")
    }
    
    compileOnly("io.papermc.paper", "paper-api", Versions.Bukkit.paper)
    shadedApi("io.papermc", "paperlib", Versions.Bukkit.paperLib)
    
    shadedApi("com.google.guava:guava:30.0-jre")
    
    shadedApi("cloud.commandframework", "cloud-paper", Versions.Libraries.cloud)
}

val jvmFlags = listOf(
    "-XX:+UseG1GC", "-XX:+ParallelRefProcEnabled", "-XX:MaxGCPauseMillis=200",
    "-XX:+UnlockExperimentalVMOptions", "-XX:+DisableExplicitGC", "-XX:+AlwaysPreTouch",
    "-XX:G1NewSizePercent=30", "-XX:G1MaxNewSizePercent=40", "-XX:G1HeapRegionSize=8M",
    "-XX:G1ReservePercent=20", "-XX:G1HeapWastePercent=5", "-XX:G1MixedGCCountTarget=4",
    "-XX:InitiatingHeapOccupancyPercent=15", "-XX:G1MixedGCLiveThresholdPercent=90",
    "-XX:G1RSetUpdatingPauseTimePercent=5", "-XX:SurvivorRatio=32", "-XX:+PerfDisableSharedMem",
    "-XX:MaxTenuringThreshold=1", "-Dusing.aikars.flags=https://mcflags.emc.gs",
    "-Daikars.new.flags=true", "-DIReallyKnowWhatIAmDoingISwear", /*"-javaagent:paperclip.jar"*/
                     )

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
        if (it.name.startsWith("Terra-bukkit") && it.name.endsWith(".jar")) it.delete() // Delete old Terra jar(s)
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
    Files.move(
        file("WorldGenTestServer/plugins").toPath(),
        file("$testDir/$dir/plugins").toPath(),
        StandardCopyOption.REPLACE_EXISTING
              )
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
    if (folder.exists()) folder.deleteRecursively()
}

fun deleteFile(file: File) {
    if (file.exists()) file.delete()
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
    
    mainClass.set("io.papermc.paperclip.Paperclip")
    jvmArgs = jvmFlags
    maxHeapSize = testMem
    minHeapSize = testMem
    args = listOf("nogui")
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
    
    mainClass.set("io.papermc.paperclip.Paperclip")
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
    relocate("org.apache.logging.slf4j", "com.dfsek.terra.lib.slf4j-over-log4j")
}


addonDir(project.file("./target/server/paper/plugins/Terra/addons"), tasks.named("runPaper").get())
addonDir(project.file("./target/server/purpur/plugins/Terra/addons"), tasks.named("runPurpur").get())
