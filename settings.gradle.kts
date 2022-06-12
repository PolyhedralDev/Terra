rootProject.name = "Terra"


fun includeImmediateChildren(dir: File, type: String) {
    dir.walkTopDown().maxDepth(1).forEach {
        if (!it.isDirectory || !File(it, "build.gradle.kts").exists()) return@forEach
        val addonDir = it.relativeTo(file(".")).path.replace("/", ":").replace("\\", ":")
        println("Including $type directory \"$addonDir\" as subproject.")
        include(addonDir)
    }
}

includeImmediateChildren(file("common/api"), "API")

includeImmediateChildren(file("common/implementation"), "implementation")

includeImmediateChildren(file("common/addons"), "addon")

includeImmediateChildren(file("platforms"), "platform")

includeImmediateChildren(file("platforms/bukkit/nms"), "Bukkit NMS")

include(":platforms:bukkit:common")

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net") {
            name = "Fabric"
        }
        maven("https://papermc.io/repo/repository/maven-public/") {
            name = "PaperMC"
        }
        gradlePluginPortal()
    }
}
