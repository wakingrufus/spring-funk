plugins {
    `java-library`
    `jvm-test-suite`
    jacoco
    id("jacoco-report-aggregation")
}
repositories {
    mavenCentral()
}
project.tasks.named("build") {
    dependsOn("jacocoTestReport")
}
testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
        }
    }
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
tasks.jacocoTestReport {
    reports {
        xml.required = true
    }
}