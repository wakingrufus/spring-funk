plugins {
    kotlin("jvm")
    funk.library
}

dependencies {
    implementation(project(":spring-funk-base"))
    implementation(project(":spring-funk-core"))
    implementation(spring.context)
    compileOnly(spring.web)
    compileOnly(libs.spring.boot.web.server)
    implementation(libs.spring.boot.core)
    implementation(libs.spring.boot.restclient)
    implementation(libs.spring.boot.starter.test)
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    api(spring.test)
    compileOnly(libs.jackson.databind)
}
