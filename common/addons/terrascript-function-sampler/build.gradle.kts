version = version("0.1.0")

dependencies {
    shadedApi(project(":common:addons:manifest-addon-loader"))
    shadedApi(project(":common:addons:config-noise-function"))
    shadedApi(project(":common:addons:structure-terrascript-loader"))
}
