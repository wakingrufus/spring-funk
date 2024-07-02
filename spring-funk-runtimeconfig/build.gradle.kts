plugins {
    kotlin("jvm")
    funk.library
}

dependencies {
    implementation(project(":spring-funk-base"))
    implementation(spring.context)
    implementation(spring.boot)
    implementation(spring.cloud.context)
    implementation(libs.slf4j)
    implementation(libs.oshai)
    implementation(kotlin("reflect"))

    testImplementation(project(":spring-funk-test"))
    testImplementation(spring.boot.test)
    testImplementation(libs.oshai)
    testImplementation(libs.assertj)
}
