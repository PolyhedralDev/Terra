preRelease(true)

versionProjects(":common:api", version("6.2.0"))
versionProjects(":common:implementation", version("6.2.0"))
versionProjects(":platforms", version("6.2.0"))


allprojects {
    group = "com.dfsek.terra"
    
    configureCompilation()
    configureDependencies()
    configurePublishing()
    
    tasks.withType<JavaCompile>().configureEach {
        options.isFork = true
        options.isIncremental = true
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
    forImmediateSubProjects(":platforms") {
        configureDistribution()
    }
    forSubProjects(":common:addons") {
        apply(plugin = "com.github.johnrengelman.shadow")
        
        tasks.named("build") {
            finalizedBy(tasks.named("shadowJar"))
        }
        
        dependencies {
            "compileOnly"(project(":common:api"))
            "testImplementation"(project(":common:api"))
        }
    }
}
