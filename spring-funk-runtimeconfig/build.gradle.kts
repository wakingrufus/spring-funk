plugins {
    kotlin("jvm")
    funk.library
}

dependencies {
    implementation(project(":spring-funk-base"))
    implementation(spring.context)
    implementation(libs.spring.boot.core)
    implementation(spring.cloud.context)
    implementation(libs.slf4j)
    implementation(libs.oshai)
    implementation(kotlin("reflect"))

    testImplementation(project(":spring-funk-test"))
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.oshai)
    testImplementation(libs.assertj)
}
