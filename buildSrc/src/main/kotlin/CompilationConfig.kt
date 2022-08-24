import com.dfsek.terra.tectonicdoc.TectonicDocPlugin
import net.ltgt.gradle.errorprone.errorprone
import net.ltgt.gradle.nullaway.nullaway
import org.apache.tools.ant.filters.ReplaceTokens
import org.checkerframework.gradle.plugin.CheckerFrameworkExtension
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
    apply(plugin = "net.ltgt.errorprone")
    apply(plugin = "net.ltgt.nullaway")
    apply(plugin = "org.checkerframework")
    
    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        doFirst {
            options.compilerArgs.add("-Xlint:all,-serial,-processing")
        }
        options.errorprone.nullaway {
            annotatedPackages.add("com.dfsek.terra")
        }
    }
    
    configure<CheckerFrameworkExtension> {
        checkers = listOf(
            //"org.checkerframework.checker.nullness.NullnessChecker",
            //"org.checkerframework.checker.units.UnitsChecker",
            //"org.checkerframework.checker.resourceleak.ResourceLeakChecker",
            //"org.checkerframework.checker.lock.LockChecker",
            //"org.checkerframework.checker.index.IndexChecker",
            //"org.checkerframework.checker.formatter.FormatterChecker",
            //"org.checkerframework.checker.optional.OptionalChecker",
            //"org.checkerframework.checker.signature.SignatureChecker",
            //"org.checkerframework.framework.util.PurityChecker",
            //"org.checkerframework.common.value.ValueChecker",
            //"org.checkerframework.common.initializedfields.InitializedFieldsChecker",
            //"org.checkerframework.common.aliasing.AliasingChecker"
                         )
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