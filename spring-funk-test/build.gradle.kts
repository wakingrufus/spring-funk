plugins {
    kotlin("jvm")
    funk.library
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
