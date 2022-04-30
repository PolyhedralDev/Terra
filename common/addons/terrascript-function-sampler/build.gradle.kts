version = version("0.1.0")

dependencies {
    compileOnly(project(":common:addons:manifest-addon-loader"))
    api(project(":common:addons:config-noise-function"))
    api(project(":common:addons:structure-terrascript-loader"))
}
