version = version("0.1.0")

dependencies {
    compileOnly(project(":common:addons:manifest-addon-loader"))
    api(project(":common:addons:chunk-generator-noise-3d"))
    api(project(":common:addons:structure-terrascript-loader"))
}
