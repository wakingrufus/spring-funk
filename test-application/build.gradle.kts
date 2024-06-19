plugins {
    application
    id("org.springframework.boot") version ("3.+")
    kotlin("jvm")
    `jvm-test-suite`
}
repositories {
    mavenCentral()
}
dependencies{
    implementation(project(":spring-funk-webmvc"))
    implementation(spring.boot.jetty)
    implementation("ch.qos.logback:logback-classic:1.5.6")

    testImplementation(spring.boot.test)
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