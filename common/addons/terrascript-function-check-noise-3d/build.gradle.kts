version = version("0.1.0")

dependencies {
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
    compileOnlyApi(project(":common:addons:chunk-generator-noise-3d"))
    compileOnlyApi(project(":common:addons:structure-terrascript-loader"))
}
