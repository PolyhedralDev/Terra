import com.dfsek.terra.configureCompilation
import com.dfsek.terra.configureDependencies

plugins {
    `java-library`
    `maven-publish`
    idea
}

configureCompilation()
configureDependencies()

group = "com.dfsek.terra.common"

dependencies {
    "shadedApi"(project(":common:api"))
}
