import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

fun Project.configureBenchmarking() {
    apply(plugin = "me.champeau.jmh")
}