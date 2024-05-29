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
