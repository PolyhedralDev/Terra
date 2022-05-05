import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
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
    
    val installAddons = tasks.create("installAddons") {
        group = "terra"
        forSubProjects(":common:addons") {
            afterEvaluate {
                dependsOn(getJarTask())
            }
        }
        
        doLast {
            // The addons are copied into a JAR because of a ShadowJar bug
            // which expands *all* JARs, even resource ones, into the fat JAR.
            // To get around this, we copy all addon JARs into a *new* JAR,
            // then, ShadowJar expands the newly created JAR, putting the original
            // JARs where they should go.
            //
            // https://github.com/johnrengelman/shadow/issues/111
            val dest = File(buildDir, "/resources/main/addons.jar")
            dest.parentFile.mkdirs()
            
            val zip = ZipOutputStream(FileOutputStream(dest))
            
            forSubProjects(":common:addons") {
                val jar = getJarTask()
                
                println("Packaging addon ${jar.archiveFileName.get()} to ${dest.absolutePath}. size: ${jar.archiveFile.get().asFile.length() / 1024}KB")
                
                val boot = if (extra.has("bootstrap") && extra.get("bootstrap") as Boolean) "bootstrap/" else ""
                
                val entry = ZipEntry("addons/$boot${jar.archiveFileName.get()}")
                zip.putNextEntry(entry)
                FileInputStream(jar.archiveFile.get().asFile).run {
                    copyTo(zip)
                    close()
                }
                zip.closeEntry()
            }
            zip.close()
        }
    }
    
    val generateResourceManifest = tasks.create("generateResourceManifest") {
        group = "terra"
        dependsOn(downloadDefaultPacks)
        dependsOn(installAddons)
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
            manifest.createNewFile()
            yaml.dump(resources, FileWriter(manifest))
        }
    }
    
    tasks["processResources"].dependsOn(generateResourceManifest)
    
    tasks.named<ShadowJar>("shadowJar") {
        // Tell shadow to download the packs
        dependsOn(downloadDefaultPacks)
        
        archiveClassifier.set("shaded")
        setVersion(project.version)
        relocate("org.apache.commons", "com.dfsek.terra.lib.commons")
        relocate("org.objectweb.asm", "com.dfsek.terra.lib.asm")
        relocate("org.json", "com.dfsek.terra.lib.json")
        relocate("org.yaml", "com.dfsek.terra.lib.yaml")
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

fun Project.getJarTask(): Jar {
    return if (tasks.findByName("shadowJar") != null) {
        (tasks.named("shadowJar").get() as ShadowJar)
    } else {
        (tasks.named("jar").get() as Jar)
    }
}