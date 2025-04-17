plugins {
    kotlin("jvm")
    funk.library
}

dependencies {
    api(project(":spring-funk-base"))
    implementation(spring.context)
    implementation(spring.boot)
    api(spring.webmvc)
    implementation(libs.slf4j)
    implementation(libs.oshai)
    api(spring.boot.autoconfigure) {
        because("api because JacksonProperties is exposed via DSL")
    }
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.17.1")

    testImplementation(project(":spring-funk-test"))
    testImplementation(spring.boot.jetty)
    testImplementation(spring.boot.tomcat)
    testImplementation(spring.boot.json)
    testImplementation(spring.boot.test)
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
                implementation(spring.boot.jetty)
                implementation(spring.boot.test)
                implementation(libs.oshai)
                implementation(libs.assertj)
            }
        }
    }
}

tasks.named("check") {
    dependsOn(testing.suites.named("aopTest"))
}
