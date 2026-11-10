version = version("1.0.0")

dependencies {
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))

    testImplementation(project(":common:implementation:base"))
    testImplementation(project(":common:addons:minecraft-attributes"))
    testImplementation(project(":common:addons:manifest-addon-loader"))
    testImplementation("com.dfsek:paralithic:${Versions.Libraries.paralithic}")
}
