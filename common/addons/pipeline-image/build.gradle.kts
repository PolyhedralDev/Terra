version = version("1.0.0")

dependencies {
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
    compileOnlyApi(project(":common:addons:biome-provider-pipeline-v2"))
    compileOnlyApi(project(":common:addons:library-image"))
}
