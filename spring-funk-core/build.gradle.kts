plugins {
    id("java-library")
    kotlin("jvm")
    `jvm-test-suite`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.assertj)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}
