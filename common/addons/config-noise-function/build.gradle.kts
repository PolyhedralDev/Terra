version = version("1.2.0")

dependencies {
    compileOnlyApi(project(":common:addons:addon-loader-manifest"))
    api("com.dfsek", "paralithic", Versions.Libraries.paralithic)
}
