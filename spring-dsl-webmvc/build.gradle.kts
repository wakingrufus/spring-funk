plugins {
    id("java-library")
    kotlin("jvm")
    `jvm-test-suite`
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":spring-dsl-base"))
    implementation(spring.context)
    implementation(spring.boot)
    api(spring.webmvc)
    implementation(libs.slf4j)
    implementation(libs.oshai)
    implementation(spring.boot.autoconfigure)
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.17.1")

    testImplementation(project(":spring-dsl-test"))
    testImplementation(spring.boot.jetty)
    testImplementation(spring.boot.test)
    testImplementation(libs.oshai)
    testImplementation(libs.assertj)
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
        }
        create<JvmTestSuite>("dslTest") {
            useJUnitJupiter()
            dependencies {
                implementation(project())
                implementation(project(":spring-dsl-test"))
                implementation(spring.boot.jetty)
                implementation(spring.boot.test)
                implementation(libs.oshai)
                implementation(libs.assertj)
            }
        }
        create<JvmTestSuite>("aopTest") {
            useJUnitJupiter()
            dependencies {
                implementation(project())
                implementation(project(":spring-dsl-test"))
                implementation(spring.boot.jetty)
                implementation(spring.boot.test)
                implementation(libs.oshai)
                implementation(libs.assertj)
            }
        }
    }
}

tasks.named("check") {
    dependsOn(testing.suites.named("dslTest"), testing.suites.named("aopTest"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}