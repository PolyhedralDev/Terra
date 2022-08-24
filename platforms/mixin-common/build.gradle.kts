plugins {
    alias(libs.plugins.mod.architectury.loom)
    alias(libs.plugins.mod.architectury.plugin)
    alias(libs.plugins.mod.loom.quiltflower)
}

loom {
    accessWidenerPath.set(file("src/main/resources/terra.accesswidener"))
    
    mixin {
        defaultRefmapName.set("terra.common.refmap.json")
    }
}

dependencies {
    shadedApi(project(":common:implementation:base"))
    
    compileOnly(libs.mod.mixin)
    compileOnly(libs.mod.fabric.fabric.loader)
    
    errorprone(libs.mod.mixin)
    errorprone(libs.mod.fabric.fabric.loader)
    
    minecraft(libs.mod.minecraft)
    mappings("net.fabricmc", "yarn", libs.versions.mod.yarn.get(), classifier = "v2")
}

architectury {
    common("fabric", "forge", "quilt")
    minecraft = libs.versions.mod.minecraft.get()
}

