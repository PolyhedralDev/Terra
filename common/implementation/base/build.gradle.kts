dependencies {
    api(project(":common:api"))
    api(project(":common:implementation:bootstrap-addon-loader"))
    
    testImplementation(libs.libraries.slf4j.api)
    
    implementation(libs.libraries.internal.apache.io)
    
    implementation(libs.libraries.internal.apache.text)
    implementation(libs.libraries.tectonic.yaml)
    
    implementation(libs.libraries.jafama)
    
    implementation(libs.libraries.paralithic)
}
