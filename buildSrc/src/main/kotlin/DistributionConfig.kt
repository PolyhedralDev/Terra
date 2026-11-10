import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml


fun Project.configureDistribution() {
    apply(plugin = "com.gradleup.shadow")

    listOf("jar", "shadowJar").forEach { taskName ->
        tasks.named<Jar>(taskName) {
            manifest.attributes(mapOf(
                "Implementation-Title" to "Terra",
                "Implementation-Version" to project.version.toString(),
                "Terra-Platform" to project.name
            ))
        }
    }

    val addonProjects = project(":common:addons").subprojects

    tasks.register("compileAddons") {
        dependsOn(addonProjects.map { "${it.path}:shadowJar" })
    }

    val generateResources = tasks.register<GenerateTerraResources>("generateTerraResources") {
        group = "terra"
        defaultPacks.set(defaultPacksTask().flatMap { it.outputDir })
        outputDir.set(layout.buildDirectory.dir("generated/terra-resources"))
    }
    configure<SourceSetContainer> {
        named("main") {
            output.dir(mapOf("builtBy" to generateResources), generateResources.flatMap { it.outputDir })
        }
    }

    gradle.projectsEvaluated {
        generateResources.configure {
            addonProjects.forEach { addon ->
                val name = addon.tasks.named<ShadowJar>("shadowJar").get().archiveFileName.get()
                (if (addon.isBootstrapAddon()) bootstrapAddons else addons).add(name)
            }
        }
    }

    bundleAddons(tasks.named<ShadowJar>("shadowJar"))

    tasks.named<ShadowJar>("shadowJar") {
        configurations.set(listOf(project.configurations.getByName("shaded")))
        archiveClassifier.set("shaded")
        version = project.version
        relocate("org.apache.commons", "com.dfsek.terra.lib.commons")
        relocate("org.objectweb.asm", "com.dfsek.terra.lib.asm")
        relocate("org.json", "com.dfsek.terra.lib.json")
        relocate("org.yaml", "com.dfsek.terra.lib.yaml")
    }

    configure<BasePluginExtension> {
        archivesName.set(project.name)
    }

    tasks.named("build") {
        dependsOn("shadowJar")
    }
}

fun Project.bundleAddons(archive: TaskProvider<out AbstractArchiveTask>) {
    val addonProjects = project(":common:addons").subprojects
    gradle.projectsEvaluated {
        archive.configure {
            addonProjects.forEach { addon ->
                from(addon.tasks.named("shadowJar")) {
                    into(if (addon.isBootstrapAddon()) "addons/bootstrap" else "addons")
                }
            }
        }
    }
}

internal fun Project.isBootstrapAddon() = extra.has("bootstrap") && extra.get("bootstrap") as Boolean

private fun Project.defaultPacksTask(): TaskProvider<DownloadDefaultPacks> {
    val root = rootProject
    if ("downloadDefaultPacks" in root.tasks.names) return root.tasks.named<DownloadDefaultPacks>("downloadDefaultPacks")
    return root.tasks.register<DownloadDefaultPacks>("downloadDefaultPacks") {
        group = "terra"
        fun release(repo: String, tag: String, file: String) =
            "https://github.com/PolyhedralDev/$repo/releases/download/$tag/$file"
        packs.put("packs/Overworld.zip", release("TerraOverworldConfig", Versions.Terra.overworldConfig, "Overworld.zip"))
        packs.put("packs/ReimagEND.zip", release("ReimagEND", Versions.Terra.reimagENDConfig, "ReimagEND.zip"))
        packs.put("packs/Tartarus.zip", release("Tartarus", Versions.Terra.tartarusConfig, "Tartarus.zip"))
        packs.put("metapacks/default.zip", release("DefaultMetapack", Versions.Terra.defaultConfig, "default.zip"))
        offline.set(root.gradle.startParameter.isOffline)
        outputDir.set(root.layout.buildDirectory.dir("default-packs"))
    }
}

abstract class DownloadDefaultPacks : DefaultTask() {
    @get:Input
    abstract val packs: MapProperty<String, String>

    @get:Internal
    abstract val offline: Property<Boolean>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    init {
        outputs.upToDateWhen { packs.get().values.none { "/latest/" in it } }
    }

    @TaskAction
    fun download() {
        val out = outputDir.get().asFile
        if (offline.get()) {
            logger.warn("Offline: using the default packs from the last build in $out")
            return
        }
        val client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build()
        val responses = packs.get().mapValues { (_, url) ->
            client.sendAsync(HttpRequest.newBuilder(URI(url)).timeout(Duration.ofMinutes(2)).build(),
                             HttpResponse.BodyHandlers.ofByteArray())
        }
        responses.forEach { (path, response) ->
            val file = File(out, path)
            val body = try {
                response.join().takeIf { it.statusCode() == 200 }?.body()
            } catch (e: Exception) {
                null
            }
            when {
                body != null -> if (!file.exists() || !file.readBytes().contentEquals(body)) {
                    file.parentFile.mkdirs()
                    file.writeBytes(body)
                }

                file.exists() -> logger.warn("Could not download ${packs.get()[path]}; using the copy from the last build")
                else -> logger.warn("Could not download ${packs.get()[path]}; jars will not include $path")
            }
        }
    }
}

abstract class GenerateTerraResources : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val defaultPacks: DirectoryProperty

    @get:Input
    abstract val addons: ListProperty<String>

    @get:Input
    abstract val bootstrapAddons: ListProperty<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val out = outputDir.get().asFile
        out.deleteRecursively()
        defaultPacks.get().asFile.copyRecursively(out)

        val resources = LinkedHashMap<String, List<String>>()
        fun put(key: String, entries: List<String>) {
            if (entries.isNotEmpty()) resources[key] = entries
        }
        fun zips(dir: String) = File(out, dir).listFiles { file -> file.name.endsWith(".zip") }?.map { it.name }?.sorted().orEmpty()
        put("addons/bootstrap", bootstrapAddons.get())
        put("addons", addons.get())
        put("packs", zips("packs"))
        put("metapacks", zips("metapacks"))

        val options = DumperOptions()
        options.indent = 2
        options.indentWithIndicator = true
        options.indicatorIndent = 2
        options.isPrettyFlow = true
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        options.defaultScalarStyle = DumperOptions.ScalarStyle.DOUBLE_QUOTED
        File(out, "resources.yml").writer().use { Yaml(options).dump(resources, it) }
    }
}
