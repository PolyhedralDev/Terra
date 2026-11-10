import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.File
import java.util.function.Predicate
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.named
import kotlin.streams.asStream


/**
 * Configures a directory where addons will be put.
 */
fun Project.addonDir(dir: File, task: Task) {
    val addonProjects = project(":common:addons").subprojects
    val moveAddons = tasks.register("moveAddons" + task.name) {
        dependsOn("compileAddons")
        // Resolved here rather than in doLast: task actions can't reach into other projects.
        val jars = addonProjects.map { addon ->
            (if (addon.isBootstrapAddon()) "bootstrap/" else "") to addon.tasks.named<ShadowJar>("shadowJar").flatMap { it.archiveFile }
        }
        doLast {
            dir.parentFile.mkdirs()
            matchingAddons(dir) {
                it.name.startsWith("Terra-") // Assume everything that starts with Terra- is a core addon.
            }.forEach {
                logger.info("Deleting old addon: " + it.absolutePath)
                it.delete()
            }
            jars.forEach { (subdirectory, jar) ->
                val file = jar.get().asFile
                val target = File(dir, subdirectory + file.name)
                logger.info("Copying addon ${file.name} to ${target.absolutePath}")
                file.copyTo(target)
            }
        }
    }
    
    task.dependsOn(moveAddons)
}

fun matchingAddons(dir: File, matcher: Predicate<File>): Set<File> {
    val matching = HashSet<File>()
    dir.walk().asStream().filter(matcher).forEach(matching::add)
    return matching
}
