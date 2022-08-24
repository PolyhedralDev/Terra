plugins {
    alias(libs.plugins.mod.architectury.loom)
    alias(libs.plugins.mod.architectury.plugin)
    alias(libs.plugins.mod.loom.quiltflower)
}

architectury {
    platformSetupLoomIde()
    loader("quilt")
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    implementation(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    "developmentQuilt"(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-common", configuration = "transformProductionQuilt")) { isTransitive = false }
    
    implementation(project(path = ":platforms:mixin-lifecycle", configuration = "namedElements")) { isTransitive = false }
    "developmentQuilt"(project(path = ":platforms:mixin-lifecycle", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-lifecycle", configuration = "transformProductionQuilt")) { isTransitive = false }
    
    compileOnly(libs.mod.mixin)
    compileOnly(libs.mod.fabric.fabric.loader)
    
    errorprone(libs.mod.mixin)
    errorprone(libs.mod.fabric.fabric.loader)
    
    minecraft(libs.mod.minecraft)
    mappings("net.fabricmc", "yarn", libs.versions.mod.yarn.get(), classifier = "v2")
    
    modImplementation(libs.mod.quilt.quilt.loader)
    modImplementation(libs.mod.quilt.fabric.api)
    
    modImplementation(libs.mod.cloud.fabric) {
        exclude("net.fabricmc")
        exclude("net.fabricmc.fabric-api")
    }
    include(libs.mod.cloud.fabric) {
        exclude("net.fabricmc")
        exclude("net.fabricmc.fabric-api")
    }
    
    modLocalRuntime(libs.mod.lazy.dfu) {
        exclude("net.fabricmc")
        exclude("net.fabricmc.fabric-api")
    }
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("src/main/resources/terra.accesswidener"))
    
    mixin {
        defaultRefmapName.set("terra.quilt.refmap.json")
    }
    
    launches {
        named("client") {
            property("fabric.log.level", "debug")
        }
        named("server") {
            property("fabric.log.level", "debug")
        }
    }
}


addonDir(project.file("./run/config/Terra/addons"), tasks.named("configureLaunch").get())

tasks {
    compileJava {
        options.release.set(17)
    }
    
    remapJar {
        injectAccessWidener.set(true)
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("${rootProject.name.capitalize()}-quilt-${project.version}.jar")
    }
}
