val platformOverrides = mapOf(
    "fabric" to "remapJar"
                             )

dependencies {
    api(project(":common:implementation:base"))
}

val taskSet = HashSet<AbstractArchiveTask>()
val resourcesDir = File("${project.buildDir}/resources/main/")

val dump = tasks.create("dumpDependents") {
    doFirst {
        taskSet.forEach {
            val resource = File(resourcesDir, it.archiveFileName.get())
            println("Including archive " + it.archiveFileName.orNull + " in directory " + resource.absolutePath)
            it.archiveFile.get().asFile.copyTo(resource, true)
        }
    }
}

tasks["processResources"].dependsOn(dump)

afterEvaluate {
    project(":platforms").subprojects.forEach {
        if (it == this@afterEvaluate) return@forEach
        
        val taskName = platformOverrides.getOrDefault(it.name, "jar")
        val task = it.tasks.named(taskName).get()
        if (task !is AbstractArchiveTask) {
            throw IllegalArgumentException("Task dependency must be Archive Task: " + task.name)
        }
        tasks["dumpDependents"].dependsOn(task)
        taskSet.add(task)
        println("Merged JAR will incorporate task ${task.name} from platform ${it.name}.")
    }
}