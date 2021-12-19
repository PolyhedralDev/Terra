import com.dfsek.terra.Versions
import com.dfsek.terra.version

version = version("0.1.0")

dependencies {
    shadedImplementation("com.dfsek.tectonic:yaml:${Versions.Libraries.tectonic}")
    shadedApi(project(":common:addons:manifest-addon-loader"))
}
