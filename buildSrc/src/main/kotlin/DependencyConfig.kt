import org.gradle.api.Project
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.repositories

fun Project.configureDependencies() {
    val testImplementation by configurations.getting
    val compileOnly by configurations.getting
    
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.fabricmc.net/") }
        maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
        maven { url = uri("https://repo.codemc.org/repository/maven-public") }
    }
    
    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")
        compileOnly("org.jetbrains:annotations:23.0.0")
        
        compileOnly("com.google.guava:guava:30.0-jre")
        testImplementation("com.google.guava:guava:30.0-jre")
    }
}