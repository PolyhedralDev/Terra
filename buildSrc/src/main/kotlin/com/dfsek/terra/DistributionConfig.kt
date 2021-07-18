package com.dfsek.terra

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginConvention
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getPlugin
import org.gradle.kotlin.dsl.named
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun Project.configureDistribution() {
    apply(plugin = "java-library")
    apply(plugin = "com.github.johnrengelman.shadow")

    val downloadDefaultPacks = tasks.create("downloadDefaultPacks") {
        group = "terra"
        doFirst {
            file("${buildDir}/resources/main/packs/").deleteRecursively()

            val defaultPackUrl = URL("https://github.com/PolyhedralDev/TerraDefaultConfig/releases/download/latest/default.zip")
            downloadPack(defaultPackUrl, project)
            val netherPackUrl = URL("https://github.com/PolyhedralDev/TerraDefaultConfig/releases/download/latest/nether.zip")
            downloadPack(netherPackUrl, project)
        }
    }

    val installAddons = tasks.create("installAddons") {
        group = "terra"
        project(":common:addons").subprojects.forEach {
            it.afterEvaluate {
                dependsOn(it.tasks.getByName("build")) // Depend on addon JARs
            }
        }

        doFirst {
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

            project(":common:addons").subprojects.forEach { addonProject ->
                val jar = (addonProject.tasks.named("jar").get() as Jar)
                println("Packaging addon ${jar.archiveFileName.get()} to ${dest.absolutePath}.")

                val entry = ZipEntry("addons/${jar.archiveFileName.get()}")
                zip.putNextEntry(entry)
                FileInputStream(jar.archiveFile.get().asFile).copyTo(zip)
                zip.closeEntry()
            }
            zip.close()
        }
    }

    val generateResourceManifest = tasks.create("generateResourceManifest") {
        group = "terra"
        dependsOn(downloadDefaultPacks)
        dependsOn(installAddons)
        doFirst {
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

            project(":common:addons").subprojects.forEach { addonProject ->
                val jar = (addonProject.tasks.named("jar").get() as Jar).archiveFileName.get()
                resources.computeIfAbsent("addons") { ArrayList() }.add(jar)
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

        configurations = listOf(project.configurations["shaded"])

        archiveClassifier.set("shaded")
        setVersion(project.version)
        relocate("org.apache.commons", "com.dfsek.terra.lib.commons")
        relocate("net.jafama", "com.dfsek.terra.lib.jafama")
        relocate("org.objectweb.asm", "com.dfsek.terra.lib.asm")
        relocate("com.google.errorprone", "com.dfsek.terra.lib.google.errorprone")
        relocate("com.google.j2objc", "com.dfsek.terra.lib.google.j2objc")
        relocate("org.checkerframework", "com.dfsek.terra.lib.checkerframework")
        relocate("org.javax.annotation", "com.dfsek.terra.lib.javax.annotation")
        relocate("org.json", "com.dfsek.terra.lib.json")
        relocate("org.yaml", "com.dfsek.terra.lib.yaml")
    }
    convention.getPlugin<BasePluginConvention>().archivesBaseName = project.name

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