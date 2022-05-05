version = version("0.1.0")

dependencies {
    api("com.googlecode.json-simple:json-simple:1.1.1")
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
}
