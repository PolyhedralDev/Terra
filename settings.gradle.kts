rootProject.name = "Terra"

include("common:implementation")

include("common:loader:addon")

fun includeImmediateChildren(dir: File, type: String) {
    dir.walkTopDown().maxDepth(1).forEach {
        if (!it.isDirectory || !File(it, "build.gradle.kts").exists()) return@forEach
        val addonDir = it.relativeTo(file(".")).path.replace("/", ":").replace("\\", ":")
        println("Including $type directory \"$addonDir\" as subproject.")
        include(addonDir)
    }
}

includeImmediateChildren(file("common/api"), "API")

includeImmediateChildren(file("common/addons"), "addon")

includeImmediateChildren(file("platforms"), "platform")

pluginManagement {
    repositories {
        maven(url = "https://maven.fabricmc.net") {
            name = "Fabric"
        }
        gradlePluginPortal()
    }
}
