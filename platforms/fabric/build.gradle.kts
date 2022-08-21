plugins {
    alias(libs.plugins.mod.architectury.loom)
    alias(libs.plugins.mod.architectury.plugin)
    alias(libs.plugins.mod.loom.quiltflower)
}

architectury {
    platformSetupLoomIde()
    loader("fabric")
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    implementation(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    "developmentFabric"(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-common", configuration = "transformProductionFabric")) { isTransitive = false }
    implementation(project(path = ":platforms:mixin-lifecycle", configuration = "namedElements")) { isTransitive = false }
    "developmentFabric"(project(path = ":platforms:mixin-lifecycle", configuration = "namedElements")) { isTransitive = false }
    shaded(project(path = ":platforms:mixin-lifecycle", configuration = "transformProductionFabric")) { isTransitive = false }
    
    minecraft(libs.mod.minecraft)
    mappings("net.fabricmc", "yarn", libs.versions.mod.yarn.get(), classifier = "v2")
    
    modImplementation(libs.mod.fabric.fabric.loader)
    
    modImplementation(libs.mod.cloud.fabric)
    include(libs.mod.cloud.fabric)
    
    modLocalRuntime(libs.mod.lazy.dfu)
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("src/main/resources/terra.accesswidener"))
    
    mixin {
        defaultRefmapName.set("terra.fabric.refmap.json")
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
        archiveFileName.set("${rootProject.name.capitalize()}-fabric-${project.version}.jar")
    }
}
