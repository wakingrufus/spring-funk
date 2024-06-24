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
    api(project(":spring-funk-base"))
    api(aws.s3)
    implementation(aws.netty)
    implementation(libs.slf4j)
    implementation(libs.oshai)
    compileOnly("io.micrometer:micrometer-core:1.13.0")
    implementation("io.netty:netty-tcnative-boringssl-static:2.0.62.Final")
    implementation("io.netty:netty-tcnative-boringssl-static:2.0.62.Final:linux-aarch_64")
    implementation("io.netty:netty-tcnative-boringssl-static:2.0.62.Final:osx-aarch_64")
    implementation("io.netty:netty-tcnative-boringssl-static:2.0.62.Final:linux-x86_64")
    implementation("io.netty:netty-tcnative-boringssl-static:2.0.62.Final:osx-x86_64")

    testImplementation(project(":spring-funk-test"))
    testImplementation(libs.oshai)
    testImplementation(libs.assertj)
    testImplementation(libs.assertj)
    testImplementation(testcontainers.junit5)
    testImplementation(testcontainers.localstack)
    testImplementation("io.micrometer:micrometer-core:1.13.0")
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
        }
        // create a separate test suite for testing when micrometer is not on classpath
        create<JvmTestSuite>("noMicrometer") {
            useJUnitJupiter()
            dependencies {
                implementation(project())
                implementation(project(":spring-funk-test"))
                implementation(libs.oshai)
                implementation(libs.assertj)
                implementation(testcontainers.junit5)
                implementation(testcontainers.localstack)
            }
        }
    }
}

tasks.named("check") {
    dependsOn(testing.suites.named("noMicrometer"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}