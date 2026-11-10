preRelease(true)

versionProjects(":common:api", version("7.0.0"))
versionProjects(":common:implementation", version("7.0.0"))
versionProjects(":platforms", version("7.0.0"))


allprojects {
    group = "com.dfsek.terra"

    configureCompilation()
    configureDependencies()
    configurePublishing()

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()

        maxHeapSize = "2G"
        ignoreFailures = false
        failFast = true
        // Projects already test in parallel, more forks per task mostly adds JVM and Minecraft bootstrap time.
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 8).coerceIn(1, 4)

        reports.html.required.set(false)
        reports.junitXml.required.set(false)
    }

    tasks.withType<Copy>().configureEach {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    tasks.withType<Jar>().configureEach {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

afterEvaluate {
    forImmediateSubProjects(":platforms") {
        configureDistribution()
    }
    project(":platforms:bukkit:common").configureDistribution()
    project(":platforms:minestom:example").configureDistribution()
    forSubProjects(":common:addons") {
        apply(plugin = "com.gradleup.shadow")

        dependencies {
            "compileOnly"(project(":common:api"))
            "testImplementation"(project(":common:api"))
        }
    }
}
