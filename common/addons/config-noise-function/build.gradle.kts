import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = version("1.1.0")

dependencies {
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
    api("com.dfsek", "paralithic", Versions.Libraries.paralithic)
}
