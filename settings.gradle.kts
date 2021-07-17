rootProject.name = "terra"

include("common:api")
include("common:implementation")

include("common:loader:config")
include("common:loader:addon")


include("common:addons:biome-provider-image")
include("common:addons:biome-provider-pipeline")
include("common:addons:biome-provider-single")

include("common:addons:chunk-generator-noise-3d")

include("common:addons:config-biome")
include("common:addons:config-carver")
include("common:addons:config-flora")
include("common:addons:config-noise-function")
include("common:addons:config-ore")
include("common:addons:config-palette")
include("common:addons:config-structure")
include("common:addons:config-tree")

include("common:addons:structure-terrascript-loader")

include("common:addons:language-yaml")



include("platforms:bukkit")
include("platforms:fabric")
include("platforms:region")
include("platforms:sponge")
include("platforms:forge")

pluginManagement {
    repositories {
        maven(url = "https://maven.fabricmc.net") {
            name = "Fabric"
        }
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.4.1"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
//        publishAlwaysIf(true)
    }
}
