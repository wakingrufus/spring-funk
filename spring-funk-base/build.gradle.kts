plugins {
    kotlin("jvm")
    funk.library
}
dependencies {
    api(project(":spring-funk-core"))
    api(spring.context)
    api(libs.spring.boot.core)
    implementation(libs.slf4j)
    implementation(kotlin("reflect"))

    testImplementation(project(":spring-funk-test"))
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.oshai)
    testImplementation(libs.assertj)
}
