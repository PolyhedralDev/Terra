version = version("0.1.0")

dependencies {
    implementation("com.dfsek.tectonic:yaml:${Versions.Libraries.tectonic}")
    compileOnly(project(":common:addons:manifest-addon-loader"))
}
