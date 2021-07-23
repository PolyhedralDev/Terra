import com.dfsek.terra.configureAddon

plugins {
    `java-library`
    `maven-publish`
    idea
}

configureAddon()

group = "com.dfsek.terra.common"

dependencies {
}
