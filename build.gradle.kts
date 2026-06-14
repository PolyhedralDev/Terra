preRelease(true)

versionProjects(":common:api", version("7.0.0"))
versionProjects(":common:implementation", version("7.0.0"))
versionProjects(":platforms", version("7.0.0"))


allprojects {
    group = "com.dfsek.terra"

    configureCompilation()
    configureDependencies()
    configurePublishing()

    tasks.withType<JavaCompile>().configureEach {
        options.isFork = true
        options.isIncremental = true
        options.release.set(21)
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()

        maxHeapSize = "2G"
        ignoreFailures = false
        failFast = true
        maxParallelForks = (Runtime.getRuntime().availableProcessors() - 1).takeIf { it > 0 } ?: 1

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
    project(":platforms:bukkit").configureDistribution()
    project(":platforms:bukkit:common").configureDistribution()
    forSubProjects(":common:addons") {
        apply(plugin = "com.gradleup.shadow")

        tasks.named("build") {
            finalizedBy(tasks.named("shadowJar"))
        }

        dependencies {
            "compileOnly"(project(":common:api"))
            "testImplementation"(project(":common:api"))
        }
    }
}

val requiredLicenseFiles = listOf(
    "LICENSE",
    "THIRD_PARTY_NOTICES.md",
    "common/implementation/LICENSE",
    "platforms/LICENSE",
    "packs/LICENSE-CC-BY-4.0.txt",
    "packs/overworld/LICENSE"
).map(layout.projectDirectory::file)

tasks.register("verifyLicenseFiles") {
    group = "verification"
    description = "Verifies required fork, upstream, and bundled pack license files."
    inputs.files(requiredLicenseFiles)

    doLast {
        val missingOrEmpty = requiredLicenseFiles
            .filter { !it.asFile.isFile || it.asFile.length() == 0L }
            .map { it.asFile.relativeTo(rootDir).invariantSeparatorsPath }
        check(missingOrEmpty.isEmpty()) {
            "Required license files are missing or empty: ${missingOrEmpty.joinToString()}"
        }

        check(file("THIRD_PARTY_NOTICES.md").readText().contains("PolyhedralDev/Terra")) {
            "THIRD_PARTY_NOTICES.md must retain the Terra source attribution."
        }
        check(file("packs/LICENSE-CC-BY-4.0.txt").readText().contains("Attribution 4.0 International")) {
            "The bundled Overworld pack must retain the CC BY 4.0 license."
        }
    }
}
