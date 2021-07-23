import com.dfsek.terra.configureCompilation
import com.dfsek.terra.configureDependencies
import com.dfsek.terra.configurePublishing

configureCompilation()
configureDependencies()
configurePublishing()



dependencies {
    "shadedApi"("com.dfsek:Paralithic:0.4.0")

    "shadedApi"("com.dfsek.tectonic:common:2.1.2")
    "shadedApi"("com.dfsek.tectonic:yaml:2.1.2")

    "shadedApi"("net.jafama:jafama:2.3.2")
}

