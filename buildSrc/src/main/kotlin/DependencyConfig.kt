import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

fun Project.configureDependencies() {
    val testImplementation = configurations.getByName("testImplementation")
    val compileOnly = configurations.getByName("compileOnly")
    
    val api = configurations.getByName("api")
    val implementation = configurations.getByName("implementation")
    
    val shaded = configurations.create("shaded")
    
    configurations.create("shadedApi") {
        shaded.extendsFrom(this)
        api.extendsFrom(this)
    }
    
    configurations.create("shadedImplementation") {
        shaded.extendsFrom(this)
        implementation.extendsFrom(this)
    }
    
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.solo-studios.ca/releases") {
            name = "Solo Studios"
        }
        maven("https://maven.solo-studios.ca/snapshots") {
            name = "Solo Studios"
        }
        maven("https://maven.fabricmc.net/") {
            name = "FabricMC"
        }
        maven("https://repo.codemc.org/repository/maven-public") {
            name = "CodeMC"
        }
        maven("https://repo.papermc.io/repository/maven-public/") {
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
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
            name = "Sonatype Snapshots"
        }
        maven("https://repo.wyck.dev/snapshots") {
            name = "Wyck Snapshots"
        }
        maven("https://repo.onarandombox.com/multiverse-releases") {
            name = "onarandombox"
        }
        maven(url = "https://central.sonatype.com/repository/maven-snapshots/") {
            content {
                includeModule("net.minestom", "minestom")
                includeModule("net.minestom", "testing")
            }
        }
    }
    
    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:${Versions.Libraries.Internal.junit}")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
        compileOnly("org.jetbrains:annotations:${Versions.Libraries.Internal.jetBrainsAnnotations}")
        
        compileOnly("com.google.guava:guava:${Versions.Libraries.Internal.guava}")
        testImplementation("com.google.guava:guava:${Versions.Libraries.Internal.guava}")
    }
}
