plugins {
    kotlin("jvm")
    funk.library
}
dependencies {
    api(project(":spring-funk-core"))
    api(spring.context)
    api(spring.boot)
    implementation(libs.slf4j)

    testImplementation(project(":spring-funk-test"))
    testImplementation(spring.boot.test)
    testImplementation(libs.oshai)
    testImplementation(libs.assertj)
}
