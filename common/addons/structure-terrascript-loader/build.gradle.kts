import com.dfsek.terra.configureAddon

plugins {
    `java-library`
    `maven-publish`
    idea
}

configureAddon()

group = "com.dfsek.terra.common"

dependencies {
    "shadedApi"("commons-io:commons-io:2.6")
}
