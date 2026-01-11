version = version("1.0.0")

dependencies {
    compileOnlyApi(project(":common:addons:addon-loader-manifest"))
    compileOnlyApi(project(":common:addons:biome-provider-pipeline"))
    compileOnlyApi(project(":common:addons:api-image"))
}
