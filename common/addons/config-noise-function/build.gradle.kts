version = version("1.2.0")

dependencies {
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
    api("com.dfsek", "paralithic", Versions.Libraries.paralithic)
}
