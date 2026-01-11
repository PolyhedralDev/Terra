version = version("1.0.0")

dependencies {
    compileOnlyApi(project(":common:addons:addon-loader-manifest"))
    api("com.dfsek", "paralithic", Versions.Libraries.paralithic)
}