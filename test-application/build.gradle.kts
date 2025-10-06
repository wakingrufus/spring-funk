plugins {
    application
    id("org.springframework.boot") version ("3.+")
    kotlin("jvm")
    `jvm-test-suite`
    jacoco
}
repositories {
    mavenCentral()
}
dependencies {
    implementation(project(":spring-funk-webmvc"))
    implementation(project(":spring-funk-htmx"))
    implementation(libs.spring.boot.starter.jetty)
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation(libs.oshai)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.starter.webflux)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass = "com.github.wakingrufus.funk.example.ExampleApplicationKt"
}