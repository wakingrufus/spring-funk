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
    implementation(project(":spring-funk-core"))
    implementation(spring.context)
    compileOnly(spring.web)
    implementation(spring.boot)
    implementation(spring.boot.test)
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    api(spring.test)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
