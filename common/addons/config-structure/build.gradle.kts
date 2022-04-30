version = version("0.1.0")

dependencies {
    api("com.googlecode.json-simple:json-simple:1.1.1")
    compileOnly(project(":common:addons:manifest-addon-loader"))
}
