import com.dfsek.terra.tectonicdoc.TectonicDocPlugin
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.filter
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.language.jvm.tasks.ProcessResources

fun Project.configureCompilation() {
    apply(plugin = "maven-publish")
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "idea")
    apply<TectonicDocPlugin>()
    
    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        doFirst {
            options.compilerArgs.add("-Xlint:all")
        }
    }
    
    tasks.withType<ProcessResources> {
        include("**/*.*")
        filter<ReplaceTokens>(
            "tokens" to mapOf(
                "DESCRIPTION" to properties["terra.description"],
                "WIKI" to properties["terra.wiki"],
                "SOURCE" to properties["terra.source"],
                "ISSUES" to properties["terra.issues"],
                "LICENSE" to properties["terra.license"]
                             )
                             )
    }
    
    afterEvaluate {
        tasks.withType<ProcessResources> {
            include("**/*.*")
            filter<ReplaceTokens>(
                "tokens" to mapOf(
                    "VERSION" to version.toString()
                                 )
                                 )
        }
    }
    
    tasks.withType<Javadoc> {
        options.encoding = "UTF-8"
    }
    
    tasks.withType<Jar> {
        archiveBaseName.set("Terra-${archiveBaseName.get()}")
        from("../LICENSE", "../../LICENSE")
    }
    
    tasks.register<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
    }
    
    tasks.register<Jar>("javadocJar") {
        dependsOn("javadoc")
        archiveClassifier.set("javadoc")
        from(tasks.getByName<Javadoc>("javadoc").destinationDir)
    }
}