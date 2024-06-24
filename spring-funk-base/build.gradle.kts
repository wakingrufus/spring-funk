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
    api(project(":spring-funk-core"))
    api(spring.context)
    api(spring.boot)
    implementation(libs.slf4j)

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
