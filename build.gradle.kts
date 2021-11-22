import com.dfsek.terra.configureCompilation
import com.dfsek.terra.configureDependencies
import com.dfsek.terra.configureDistribution
import com.dfsek.terra.configurePublishing
import com.dfsek.terra.getGitHash

val versionObj = Version("6", "0", "0", true)

allprojects {
    version = versionObj
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

/**
 * Version class that does version stuff.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Version(val major: String, val minor: String, val revision: String, val preRelease: Boolean = false) {
    
    override fun toString(): String {
        return if (!preRelease)
            "$major.$minor.$revision"
        else //Only use git hash if it's a prerelease.
            "$major.$minor.$revision-BETA+${getGitHash()}"
    }
}
