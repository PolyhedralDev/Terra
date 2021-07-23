import com.dfsek.terra.configureCompilation
import com.dfsek.terra.configureDependencies

configureCompilation()
configureDependencies()



dependencies {
    "shadedApi"(project(":common:api"))
}
