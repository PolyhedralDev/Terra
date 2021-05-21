import com.dfsek.terra.configureCommon
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecraftforge.gradle.common.util.RunConfig
import net.minecraftforge.gradle.userdev.UserDevExtension
import net.minecraftforge.gradle.userdev.tasks.RenameJarInPlace

buildscript {
    repositories {
        maven { url = uri("https://files.minecraftforge.net/maven") }
        jcenter()
        mavenCentral()
        maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
    }
    dependencies {
        classpath(group = "net.minecraftforge.gradle", name = "ForgeGradle", version = "4.1.+")
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}
apply(plugin = "net.minecraftforge.gradle")
apply(plugin = "org.spongepowered.mixin")

configure<org.spongepowered.asm.gradle.plugins.MixinExtension> {
    add(sourceSets.main.get(), "terra-refmap.json")
}

plugins {
    java
    id("com.modrinth.minotaur").version("1.1.0")
}

configureCommon()

group = "com.dfsek.terra.forge"

repositories {
    maven { url = uri("https://files.minecraftforge.net/maven") }
    jcenter()
    mavenCentral()
    maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
}

val forgeVersion = "36.1.13"
val mcVersion = "1.16.5"
dependencies {
    "shadedApi"(project(":common"))
    "minecraft"("net.minecraftforge:forge:$mcVersion-$forgeVersion")
    "annotationProcessor"("org.spongepowered:mixin:0.8.2:processor")
}

if ("true" == System.getProperty("idea.sync.active")) {
    afterEvaluate {
        tasks.withType<JavaCompile>().all {
            options.annotationProcessorPath = files()
        }
    }
}


tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set(tasks.getByName<Jar>("jar").archiveBaseName.orNull) // Pain. Agony, even.
    archiveClassifier.set("") // Suffering, if you will.
}

afterEvaluate {
    tasks.named<RenameJarInPlace>("reobfJar") {
        val shadow = tasks.getByName<ShadowJar>("shadowJar");
        dependsOn(shadow)
        input = shadow.archiveFile.orNull?.asFile
    }
}

configure<UserDevExtension> {
    mappings(mapOf(
            "channel" to "official",
            "version" to mcVersion
    ))
    runs {
        val runConfig = Action<RunConfig> {
            properties(mapOf(
                    //"forge.logging.markers" to "SCAN,REGISTRIES,REGISTRYDUMP",
                    "forge.logging.console.level" to "debug"
            ))
            arg("-mixin.config=terra.mixins.json")
            workingDirectory = project.file("run").canonicalPath
            source(sourceSets["main"])
        }
        create("client", runConfig)
        create("server", runConfig)
    }
}

tasks.register<Jar>("deobfJar") {
    from(sourceSets["main"].output)
    archiveClassifier.set("dev")
    group = "forge"
}

val deobfElements = configurations.register("deobfElements") {
    isVisible = false
    description = "De-obfuscated elements for libs"
    isCanBeResolved = false
    isCanBeConsumed = true
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage.JAVA_API))
        attribute(Category.CATEGORY_ATTRIBUTE, project.objects.named(Category.LIBRARY))
        attribute(Bundling.BUNDLING_ATTRIBUTE, project.objects.named(Bundling.EXTERNAL))
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, project.objects.named(LibraryElements.JAR))
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
    }
    outgoing.artifact(tasks.named("deobfJar"))
}

val javaComponent = components["java"] as AdhocComponentWithVariants
javaComponent.addVariantsFromConfiguration(deobfElements.get()) {
    mapToMavenScope("runtime")
}

tasks.jar {
    manifest {
        attributes(mapOf(
                "Specification-Title" to "terra",
                "Specification-Vendor" to "Terra",
                "Specification-Version" to "1.0",
                "Implementation-Title" to "Terra",
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to "terra",
                "MixinConfigs" to "terra.mixins.json"
        ))
    }
}

tasks.register<com.modrinth.minotaur.TaskModrinthUpload>("publishModrinthForge") {
    dependsOn("reobfJar")
    group = "forge"
    token = System.getenv("MODRINTH_SECRET")
    projectId = "FIlZB9L0"
    versionNumber = "${project.version}-forge"
    uploadFile = tasks.named<RenameJarInPlace>("reobfJar").get().input.absoluteFile
    releaseType = "alpha"
    addGameVersion("1.16.5")
    addLoader("forge")
}