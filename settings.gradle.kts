rootProject.name = "spring-dsl"

include(":spring-dsl-core")
include(":spring-dsl-base")
include(":spring-dsl-test")
include(":spring-dsl-runtimeconfig")

dependencyResolutionManagement {
    versionCatalogs {
        create("spring") {
            version("spring", "6.1.6")
            version("boot", "3.2.4")
            version("cloud", "4.1.2")
            library("context", "org.springframework", "spring-context").versionRef("spring")
            library("cloud.context", "org.springframework.cloud", "spring-cloud-context").versionRef("cloud")
            library("test", "org.springframework", "spring-test").versionRef("spring")
            library("boot", "org.springframework.boot", "spring-boot").versionRef("boot")
            library("boot.test", "org.springframework.boot", "spring-boot-starter-test").versionRef("boot")
        }
        create("libs") {
            library("assertj", "org.assertj", "assertj-core").version("3.16.1")
            version("slf4j", "2.0.13")
            library("slf4j", "org.slf4j", "slf4j-api").versionRef("slf4j")
            library("oshai", "io.github.oshai", "kotlin-logging-jvm").version("5.1.0")
        }
    }
}