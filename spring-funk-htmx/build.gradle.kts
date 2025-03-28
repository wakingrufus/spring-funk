plugins {
    kotlin("jvm")
    funk.library
}

dependencies {
    api(project(":spring-funk-base"))
    implementation(project(":spring-funk-webmvc"))
    implementation(libs.slf4j)
    implementation(libs.oshai)
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")

    api("org.jetbrains.kotlinx:kotlinx-html-jvm:0.12.0")
    api("org.jetbrains.kotlin-wrappers:kotlin-css-jvm:2025.3.20")

    testImplementation(project(":spring-funk-test"))
    testImplementation(spring.boot.test)
    testImplementation(libs.oshai)
    testImplementation(libs.assertj)
    testImplementation(libs.assertj)
    testImplementation(spring.boot.jetty)
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
        }
    }
}
