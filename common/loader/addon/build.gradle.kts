import com.dfsek.terra.configureCompilation
import com.dfsek.terra.configureDependencies

configureCompilation()
configureDependencies()

group = "com.dfsek.terra.common"

dependencies {
    "shadedApi"(project(":common:api"))
}
