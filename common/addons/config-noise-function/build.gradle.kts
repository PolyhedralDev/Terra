plugins {
    id("me.champeau.jmh") version "0.6.6"
}

version = version("0.1.0")

dependencies {
    shadedApi(project(":common:addons:manifest-addon-loader"))
    jmh("org.openjdk.jmh:jmh-core:1.35")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:1.35")
}
