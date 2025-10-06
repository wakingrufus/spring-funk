plugins {
    kotlin("jvm")
    funk.library
}

dependencies {
    api(project(":spring-funk-base"))
    implementation(project(":spring-funk-webmvc"))
    implementation(libs.slf4j)
    implementation(libs.oshai)
    implementation(libs.jackson.databind)

    api("io.github.wakingrufus:khtmx-dsl-jvm:0.1.0")
    api("io.github.wakingrufus:khtmx-spring:0.1.0")
    api("org.jetbrains.kotlin-wrappers:kotlin-css-jvm:2025.3.20")

    testImplementation(project(":spring-funk-test"))
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(spring.test)
    testImplementation(libs.oshai)
    testImplementation(libs.assertj)
    testImplementation(libs.spring.boot.starter.jetty)
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
        }
    }
}
