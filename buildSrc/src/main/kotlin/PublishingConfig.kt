import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType

fun Project.configurePublishing() {
    tasks.withType<AbstractPublishToMaven>().configureEach {
        onlyIf("the project has a version") { publication.version != Project.DEFAULT_VERSION }
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
            }
        }
        
        repositories {
            val mavenUrl = "https://maven.solo-studios.ca/releases/"
            //val mavenSnapshotUrl = "https://repo.codemc.io/repository/maven-snapshots/"
            
            maven(mavenUrl) {
                val SoloStudiosReleasesUsername: String? by project
                val SoloStudiosReleasesPassword: String? by project
                
                if (SoloStudiosReleasesUsername != null && SoloStudiosReleasesPassword != null) {
                    credentials {
                        username = SoloStudiosReleasesUsername
                        password = SoloStudiosReleasesPassword
                    }
                }
            }
        }
    }
}