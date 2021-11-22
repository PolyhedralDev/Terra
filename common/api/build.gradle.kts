afterEvaluate {
    subprojects.forEach {
        if(it != project) {
            println("Project: ${it.name}")
            dependencies {
                shadedApi(it)
            }
        }
    }
}