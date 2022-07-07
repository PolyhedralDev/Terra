import org.gradle.api.Project
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

fun Project.configureDependencies() {
    val testImplementation by configurations.getting
    val compileOnly by configurations.getting
    
    val api by configurations.getting
    val implementation by configurations.getting
    
    val shaded by configurations.creating
    
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
    }
    
    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")
        compileOnly("org.jetbrains:annotations:23.0.0")
        
        compileOnly("com.google.guava:guava:30.0-jre")
        testImplementation("com.google.guava:guava:30.0-jre")
    }
}