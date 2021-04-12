package com.dfsek.terra

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginConvention
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.kotlin.dsl.*
import java.io.File
import java.net.URL

fun Project.configureDistribution() {
    apply(plugin = "java-library")
    apply(plugin = "com.github.johnrengelman.shadow")

    configurations {
        val shaded = create("shaded")
        getByName("compile").extendsFrom(shaded)
        val shadedApi = create("shadedApi")
        shaded.extendsFrom(shadedApi)
        getByName("api").extendsFrom(shadedApi)
        val shadedImplementation = create("shadedImplementation")
        shaded.extendsFrom(shadedImplementation)
        getByName("implementation").extendsFrom(shadedImplementation)
    }

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
    tasks["processResources"].dependsOn(downloadDefaultPacks)

    tasks.withType<Jar> {
        archiveBaseName.set("Terra-${archiveBaseName.get()}")
        from("../LICENSE", "../../LICENSE")
    }

    tasks.register<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
    }

    tasks.register<Jar>("javadocJar") {
        dependsOn("javadoc")
        archiveClassifier.set("javadoc")
        from(tasks.getByName<Javadoc>("javadoc").destinationDir)
    }

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
        minimize()
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