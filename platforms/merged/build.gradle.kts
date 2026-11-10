val platformOverrides = mapOf(
    "fabric" to "shadowJar"
)

dependencies {
    api(project(":common:implementation:base"))
}

val dump = tasks.register<Sync>("dumpDependents") {
    into(layout.buildDirectory.dir("generated/platform-jars"))
}

sourceSets.main {
    output.dir(mapOf("builtBy" to dump), dump.map { it.destinationDir })
}

afterEvaluate {
    project(":platforms").subprojects.forEach {
        if (it == this@afterEvaluate) return@forEach

        val taskName = platformOverrides.getOrDefault(it.name, "jar")
        val task = it.tasks.named(taskName)
        dump.configure { from(task) }
        logger.info("Merged JAR will incorporate task $taskName from platform ${it.name}.")
    }
}
