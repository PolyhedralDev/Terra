import org.gradle.api.Project
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.apply

fun Project.configureDependencies() {
    val testImplementation by configurations.getting
    val compileOnly by configurations.getting
    
    val api by configurations.getting
    val implementation by configurations.getting
    
    val shaded by configurations.creating
    
    val libs = rootProject.project.versionCatalogs.libs
    
    @Suppress("UNUSED_VARIABLE")
    val shadedApi by configurations.creating {
        shaded.extendsFrom(this)
        api.extendsFrom(this)
    }
    
    @Suppress("UNUSED_VARIABLE")
    val shadedImplementation by configurations.creating {
        shaded.extendsFrom(this)
        implementation.extendsFrom(this)
    }
    
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") {
            name = "FabricMC"
        }
        maven("https://repo.codemc.org/repository/maven-public") {
            name = "CodeMC"
        }
        maven("https://papermc.io/repo/repository/maven-public/") {
            name = "PaperMC"
        }
        maven("https://files.minecraftforge.net/maven/") {
            name = "Forge"
        }
        maven("https://maven.quiltmc.org/repository/release/") {
            name = "Quilt"
        }
        maven("https://jitpack.io") {
            name = "JitPack"
        }
        maven("https://api.modrinth.com/maven") {
            name = "Modrinth"
        }
    }
    
    dependencies {
        testImplementation(libs.findLibrary("libraries.internal.junit.jupiter.api").get())
        testImplementation(libs.findLibrary("libraries.internal.junit.jupiter.engine").get())
        compileOnly(libs.findLibrary("libraries.internal.jetbrains.annotations").get())
    
        compileOnly(libs.findLibrary("libraries.guava").get())
        testImplementation(libs.findLibrary("libraries.guava").get())
    
        "errorprone"(libs.findLibrary("libraries_internal_error-prone").get())
        //"errorprone"(libs.findLibrary("libraries_internal_nullaway").get())
    }
}