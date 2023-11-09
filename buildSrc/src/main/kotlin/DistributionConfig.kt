import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.File
import java.io.FileWriter
import java.net.URI
import java.net.URL
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.named
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import kotlin.io.path.copyTo
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists


fun Project.configureDistribution() {
    apply(plugin = "com.github.johnrengelman.shadow")
    
    val downloadDefaultPacks = tasks.create("downloadDefaultPacks") {
        group = "terra"
        doFirst {
            file("${buildDir}/resources/main/packs/").deleteRecursively()
            val defaultPackUrl = URL("https://github.com/PolyhedralDev/TerraOverworldConfig/releases/download/latest/default.zip")
            downloadPack(defaultPackUrl, project)
        }
    }
    
    val compileAddons = tasks.create("compileAddons") {
        forSubProjects(":common:addons") {
            afterEvaluate {
                dependsOn(getJarTask())
            }
        }
    }
    
    val installAddons = tasks.create("installAddons") {
        group = "terra"
        dependsOn(compileAddons)
        
        doLast {
            // https://github.com/johnrengelman/shadow/issues/111
            val dest = URI.create("jar:" + tasks.named<ShadowJar>("shadowJar").get().archiveFile.get().asFile.toURI())
            
            val provider = try {
                FileSystems.getFileSystem(dest)
            } catch (e: FileSystemNotFoundException) {
                null
            } ?: FileSystems.newFileSystem(dest, mapOf("create" to "false"), null)
            provider?.use { fs ->
                forSubProjects(":common:addons") {
                    if (fs.isOpen) {
                        val jar = getJarTask()
                        
                        if (jar.archiveFile.get().asFile.exists()) {
                            println("Packaging addon ${jar.archiveFileName.get()} to $dest. size: ${jar.archiveFile.get().asFile.length() / 1024}KB")
                            
                            val boot = if (extra.has("bootstrap") && extra.get("bootstrap") as Boolean) "bootstrap/" else ""
                            val addonPath = fs.getPath("/addons/$boot${jar.archiveFileName.get()}")
                            
                            if (!addonPath.exists()) {
                                addonPath.parent.createDirectories()
                                addonPath.createFile()
                                jar.archiveFile.get().asFile.toPath().copyTo(addonPath, overwrite = true)
                            }
                        }
                    }
                    
                }
            }
        }
    }
    
    val generateResourceManifest = tasks.create("generateResourceManifest") {
        group = "terra"
        doLast {
            val resources = HashMap<String, MutableList<String>>()
            val packsDir = File("${project.buildDir}/resources/main/packs/")
            
            packsDir.walkTopDown().forEach {
                if (it.isDirectory || !it.name.endsWith(".zip")) return@forEach
                resources.computeIfAbsent("packs") { ArrayList() }.add(it.name)
            }
            
            val langDir = File("${project(":common:implementation").buildDir}/resources/main/lang/")
            
            langDir.walkTopDown().forEach {
                if (it.isDirectory || !it.name.endsWith(".yml")) return@forEach
                resources.computeIfAbsent("lang") { ArrayList() }.add(it.name)
            }
            
            forSubProjects(":common:addons") {
                val jar = getJarTask().archiveFileName.get()
                resources.computeIfAbsent(
                    if (extra.has("bootstrap") && extra.get("bootstrap") as Boolean) "addons/bootstrap"
                    else "addons"
                                         ) { ArrayList() }.add(jar)
            }
            
            val options = DumperOptions()
            options.indent = 2
            options.indentWithIndicator = true
            options.indicatorIndent = 2
            options.isPrettyFlow = true
            options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
            options.defaultScalarStyle = DumperOptions.ScalarStyle.DOUBLE_QUOTED
            
            val yaml = Yaml(options)
            
            val manifest = File("${project.buildDir}/resources/main/resources.yml")
            
            if (manifest.exists()) manifest.delete()
            manifest.parentFile.mkdirs()
            manifest.createNewFile()
            FileWriter(manifest).use {
                yaml.dump(resources, it)
            }
            
        }
    }
    
    tasks.named("processResources") {
        generateResourceManifest.mustRunAfter(downloadDefaultPacks)
        finalizedBy(downloadDefaultPacks)
        finalizedBy(generateResourceManifest)
    }
    
    
    tasks.named<ShadowJar>("shadowJar") {
        // Tell shadow to download the packs
        dependsOn(downloadDefaultPacks)
        configurations = listOf(project.configurations["shaded"])
        archiveClassifier.set("shaded")
        version = project.version
        relocate("org.apache.commons", "com.dfsek.terra.lib.commons")
        relocate("org.objectweb.asm", "com.dfsek.terra.lib.asm")
        relocate("com.dfsek.paralithic", "com.dfsek.terra.lib.paralithic")
        relocate("org.json", "com.dfsek.terra.lib.json")
        relocate("org.yaml", "com.dfsek.terra.lib.yaml")
        
        finalizedBy(installAddons)
    }
    
    configure<BasePluginExtension> {
        archivesName.set(project.name)
    }
    
    tasks.named<DefaultTask>("build") {
        dependsOn(tasks["shadowJar"])
    }
}

fun downloadPack(packUrl: URL, project: Project) {
    val fileName = packUrl.file.substring(packUrl.file.lastIndexOf("/"))
    val file = File("${project.buildDir}/resources/main/packs/${fileName}")
    file.parentFile.mkdirs()
    file.outputStream().write(packUrl.readBytes())
}

fun Project.getJarTask() = tasks.named("shadowJar").get() as ShadowJar
