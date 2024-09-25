import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = version("1.2.0")

dependencies {
    api("commons-io", "commons-io", Versions.Libraries.Internal.apacheIO)
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
}