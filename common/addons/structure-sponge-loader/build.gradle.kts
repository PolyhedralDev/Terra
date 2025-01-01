import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = version("1.0.0")

dependencies {
    api("commons-io", "commons-io", Versions.Libraries.Internal.apacheIO)
    api("com.github.Querz", "NBT", Versions.Libraries.Internal.nbt)
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
}