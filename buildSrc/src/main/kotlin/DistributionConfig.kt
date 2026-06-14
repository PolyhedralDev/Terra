import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.papermc.paperweight.util.path
import java.io.File
import java.io.FileWriter
import java.nio.file.FileSystems
import java.nio.file.Path
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.api.tasks.bundling.Zip
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
    
    val bundleOverworldPack = tasks.register("bundleOverworldPack", Zip::class.java) {
        group = "terra"
        description = "Bundles the pinned TerraOverworldConfig submodule."

        val packDirectory = rootProject.file("packs/overworld")
        inputs.dir(packDirectory)
        archiveFileName.set("Overworld.zip")
        destinationDirectory.set(layout.buildDirectory.dir("resources/main/packs"))
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true

        from(packDirectory) {
            exclude(
                ".git",
                ".github/**",
                ".scripts/**",
                ".wiki/**",
                ".gitignore",
                ".gitmodules",
                "CHANGELOG.md",
                "DESIGN.md",
                "README.md"
            )
        }

        doFirst {
            if (!packDirectory.resolve("pack.yml").isFile) {
                throw GradleException(
                    "Pinned Overworld pack is missing. Run " +
                    "'git submodule update --init packs/overworld'."
                )
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
        finalizedBy(generateResourceManifest)
    }

    bundleOverworldPack.configure {
        mustRunAfter(tasks.named("processResources"))
    }

    generateResourceManifest.dependsOn(bundleOverworldPack)

    tasks.named<Jar>("jar") {
        dependsOn(generateResourceManifest)
    }
    
    
    tasks.named<ShadowJar>("shadowJar") {
        dependsOn(generateResourceManifest)
        configurations = listOf(project.configurations["shaded"])
        archiveClassifier.set("shaded")
        version = project.version
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

fun Project.getJarTask() = tasks.named("shadowJar").get() as ShadowJar
