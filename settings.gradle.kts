plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "spring-dsl"

include(":spring-dsl-core")
include(":spring-dsl-base")
include(":spring-dsl-test")
include(":spring-dsl-runtimeconfig")
include(":spring-dsl-webmvc")
include(":test-application")
include(":spring-dsl-aws")

dependencyResolutionManagement {
    versionCatalogs {
        create("spring") {
            version("spring", "6.1.6")
            version("boot", "3.2.4")
            version("cloud", "4.1.2")
            library("context", "org.springframework", "spring-context").versionRef("spring")
            library("web", "org.springframework", "spring-web").versionRef("spring")
            library("webmvc", "org.springframework", "spring-webmvc").versionRef("spring")
            library("cloud.context", "org.springframework.cloud", "spring-cloud-context").versionRef("cloud")
            library("test", "org.springframework", "spring-test").versionRef("spring")
            library("boot", "org.springframework.boot", "spring-boot").versionRef("boot")
            library("boot.test", "org.springframework.boot", "spring-boot-starter-test").versionRef("boot")
            library("boot.jetty", "org.springframework.boot", "spring-boot-starter-jetty").versionRef("boot")
            library("boot.json", "org.springframework.boot", "spring-boot-starter-json").versionRef("boot")
            library("boot.tomcat", "org.springframework.boot", "spring-boot-starter-tomcat").versionRef("boot")
            library("boot.autoconfigure", "org.springframework.boot", "spring-boot-autoconfigure")
                .versionRef("boot")
        }
        create("libs") {
            library("assertj", "org.assertj", "assertj-core").version("3.16.1")
            version("slf4j", "2.0.13")
            library("slf4j", "org.slf4j", "slf4j-api").versionRef("slf4j")
            library("oshai", "io.github.oshai", "kotlin-logging-jvm").version("5.1.0")
        }
        create("aws") {
            version("awssdk", "2.25.60")
            library("netty", "software.amazon.awssdk", "netty-nio-client").versionRef("awssdk")
            library("s3", "software.amazon.awssdk", "s3").versionRef("awssdk")

        }
        create("testcontainers") {
            version("testcontainers", "1.19.3")
            library("junit5", "org.testcontainers", "junit-jupiter").versionRef("testcontainers")
            library("core", "org.testcontainers", "testcontainers").versionRef("testcontainers")
            library("localstack", "org.testcontainers", "localstack").versionRef("testcontainers")
        }
    }
}