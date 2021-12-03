import com.dfsek.terra.version

version = version("0.1.0")

dependencies {
    shadedApi(project(":common:addons:manifest-addon-loader"))
    shadedApi(project(":common:addons:chunk-generator-noise-3d"))
}
