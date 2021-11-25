import com.dfsek.terra.configureCompilation
import com.dfsek.terra.configureDependencies
import com.dfsek.terra.configureDistribution
import com.dfsek.terra.configurePublishing
import com.dfsek.terra.version
import com.dfsek.terra.versionProjects

versionProjects(":common:api", version("6.0.0"))
versionProjects(":common:implementation", version("6.0.0"))
versionProjects(":platforms", version("6.0.0"))


allprojects {
    group = "com.dfsek.terra"
    
    configureDependencies()
    configureCompilation()
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
    project(":platforms").subprojects.forEach { // Platform projects are where distribution happens
        it.configureDistribution()
    }
}
