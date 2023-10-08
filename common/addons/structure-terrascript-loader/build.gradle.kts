import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = version("1.1.0")

dependencies {
    api("commons-io:commons-io:2.7")
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
}
