plugins {
    alias(libs.plugins.mod.architectury.loom)
    alias(libs.plugins.mod.architectury.plugin)
    alias(libs.plugins.mod.loom.quiltflower)
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    implementation(project(path = ":platforms:mixin-common", configuration = "namedElements")) { isTransitive = false }
    
    shadedApi(project(":common:implementation:base"))
    
    compileOnly(libs.mod.mixin)
    compileOnly(libs.mod.fabric.fabric.loader)
    
    errorprone(libs.mod.mixin)
    errorprone(libs.mod.fabric.fabric.loader)
    
    minecraft(libs.mod.minecraft)
    mappings("net.fabricmc", "yarn", libs.versions.mod.yarn.get(), classifier = "v2")
    
    modImplementation(libs.mod.cloud.fabric) {
        exclude("net.fabricmc")
        exclude("net.fabricmc.fabric-api")
    }
}

loom {
    accessWidenerPath.set(project(":platforms:mixin-common").file("src/main/resources/terra.accesswidener"))
    
    mixin {
        defaultRefmapName.set("terra.lifecycle.refmap.json")
    }
}

tasks {
    compileJava {
        options.release.set(17)
    }
    
    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
    }
}

architectury {
    common("fabric", "quilt")
    minecraft = libs.versions.mod.minecraft.get()
}