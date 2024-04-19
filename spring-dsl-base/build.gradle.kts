plugins {
    id("java-library")
    kotlin("jvm")
    `jvm-test-suite`
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":spring-dsl-core"))
    implementation(spring.context)
    implementation(spring.boot)
    implementation(libs.slf4j)

    testImplementation(project(":spring-dsl-test"))
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