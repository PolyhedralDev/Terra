dependencies {
    api(libs.libraries.strata)
    compileOnlyApi(libs.libraries.slf4j.api)
    testImplementation(libs.libraries.slf4j.api)
    api(libs.libraries.cloud.core)
    
    api(libs.libraries.tectonic)
    
    api(libs.libraries.caffeine)
    
    api(libs.libraries.vavr)
    
    api(libs.libraries.jafama)
    
    api(libs.libraries.paralithic)
}