import com.dfsek.terra.version

version = version("0.1.0")

dependencies {
    shadedImplementation("com.dfsek.tectonic:yaml:2.1.2")
    shadedApi(project(":common:addons:manifest-addon-loader"))
}
