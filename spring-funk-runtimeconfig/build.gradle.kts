plugins {
    id("java-library")
    kotlin("jvm")
    `jvm-test-suite`
    jacoco
}

repositories {
    mavenCentral()
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

testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}