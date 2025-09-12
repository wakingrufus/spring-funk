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
    implementation(libs.jackson.datatype.jdk8)

    api("io.github.wakingrufus:khtmx-dsl-jvm:0.0.10")
    api("org.jetbrains.kotlin-wrappers:kotlin-css-jvm:2025.3.20")

    testImplementation(project(":spring-funk-test"))
    testImplementation(spring.boot.test)
    testImplementation(spring.test)
    testImplementation(libs.oshai)
    testImplementation(libs.assertj)
    testImplementation(spring.boot.jetty)
    testImplementation(libs.jackson.datatype.jdk8)
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
        }
    }
}
