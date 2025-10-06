plugins {
    kotlin("jvm")
    funk.library
}

dependencies {
    api(project(":spring-funk-base"))
    implementation(spring.context)
    implementation(libs.spring.boot.core)
    implementation(libs.spring.boot.web.server)
    compileOnly(libs.spring.boot.tomcat)
    compileOnly(libs.spring.boot.jetty)
    api(spring.webmvc)
    implementation(libs.slf4j)
    implementation(libs.oshai)
    api(libs.spring.boot.autoconfigure) {
        because("api because JacksonProperties is exposed via DSL")
    }
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    compileOnly(libs.jackson.databind)

    testImplementation(project(":spring-funk-test"))
    testImplementation(libs.spring.boot.starter.jetty)
    testImplementation(libs.spring.boot.starter.tomcat)
    testImplementation(libs.spring.boot.starter.json)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.oshai)
    testImplementation(libs.assertj)
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
}

testing {
    suites {
        // create a separate test suite for testing AOP app since it is classpath-dependant
        create<JvmTestSuite>("aopTest") {
            useJUnitJupiter()
            dependencies {
                implementation(project())
                implementation(project(":spring-funk-test"))
                implementation(libs.spring.boot.starter.jetty)
                implementation(libs.spring.boot.starter.test)
                implementation(libs.oshai)
                implementation(libs.assertj)
            }
        }
    }
}

tasks.named("check") {
    dependsOn(testing.suites.named("aopTest"))
}
