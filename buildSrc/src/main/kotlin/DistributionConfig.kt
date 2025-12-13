import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.papermc.paperweight.util.path
import java.io.File
import java.io.FileWriter
import java.net.URL
import java.nio.file.FileSystems
import java.nio.file.Path
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.jvm.tasks.Jar
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

private fun Project.installAddonsInto(dest: Path) {
    FileSystems.newFileSystem(dest, mapOf("create" to "false"), null).use { fs ->
        forSubProjects(":common:addons") {
            val jar = getJarTask()
            
            logger.info("Packaging addon ${jar.archiveFileName.get()} to $dest. size: ${jar.archiveFile.get().asFile.length() / 1024}KB")
            
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

fun Project.configureDistribution() {
    apply(plugin = "com.gradleup.shadow")
    
    val downloadDefaultPacks = tasks.create("downloadDefaultPacks") {
        group = "terra"
        doFirst {
            try {
                file("${buildDir}/resources/main/packs/").deleteRecursively()
                file("${buildDir}/resources/main/metapacks/").deleteRecursively()
                val overworldPackUrl =
                    URL("https://github.com/PolyhedralDev/TerraOverworldConfig/releases/download/" + Versions.Terra.overworldConfig + "/Overworld.zip")
                val reimagENDPackUrl =
                    URL("https://github.com/PolyhedralDev/ReimagEND/releases/download/" + Versions.Terra.reimagENDConfig + "/ReimagEND.zip")
                val tartarusPackUrl =
                    URL("https://github.com/PolyhedralDev/Tartarus/releases/download/" + Versions.Terra.tartarusConfig + "/Tartarus.zip")
                val defaultPackUrl =
                    URL("https://github.com/PolyhedralDev/DefaultMetapack/releases/download/" + Versions.Terra.defaultConfig + "/default.zip")
                downloadPack(overworldPackUrl, project)
                downloadPack(reimagENDPackUrl, project)
                downloadPack(tartarusPackUrl, project)
                downloadPack(defaultPackUrl, project, true)
            } catch (_: Exception) {
            }
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
            val dest = tasks.named<ShadowJar>("shadowJar").get().archiveFile.get().path
            installAddonsInto(dest)
        }
    }
    
    tasks.create("installAddonsIntoDefaultJar") {
        group = "terra"
        dependsOn(compileAddons)
        
        doLast {
            val dest = tasks.named<Jar>("jar").get().archiveFile.get().path
            installAddonsInto(dest)
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
            
            val metaPacksDir = File("${project.buildDir}/resources/main/metapacks/")
            
            metaPacksDir.walkTopDown().forEach {
                if (it.isDirectory || !it.name.endsWith(".zip")) return@forEach
                resources.computeIfAbsent("metapacks") { ArrayList() }.add(it.name)
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
        relocate("com.github.benmanes.caffeine", "com.dfsek.terra.lib.caffeine")
        relocate("org.apache.commons", "com.dfsek.terra.lib.commons")
        relocate("org.objectweb.asm", "com.dfsek.terra.lib.asm")
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

fun downloadPack(packUrl: URL, project: Project, metapack: Boolean = false) {
    val fileName = packUrl.file.substring(packUrl.file.lastIndexOf("/"))
    val resourceType = if (metapack) "metapacks" else "packs"
    val file = File("${project.buildDir}/resources/main/${resourceType}/${fileName}")
    file.parentFile.mkdirs()
    file.outputStream().write(packUrl.readBytes())
}

fun Project.getJarTask() = tasks.named("shadowJar").get() as ShadowJar
