version = version("1.0.0")

dependencies {
    implementation(libs.libraries.tectonic.yaml)
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
}
