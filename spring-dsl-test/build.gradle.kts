plugins {
    id("java-library")
    kotlin("jvm")
    `jvm-test-suite`
}

repositories {
    mavenCentral()
}


dependencies {
    implementation(project(":spring-dsl-base"))
    implementation(project(":spring-dsl-core"))
    implementation(spring.context)
    implementation(spring.boot)
    implementation(spring.test)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
