plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "spring-funk"

include(":spring-funk-core")
include(":spring-funk-base")
include(":spring-funk-test")
include(":spring-funk-runtimeconfig")
include(":spring-funk-webmvc")
include(":test-application")
include(":spring-funk-aws")

dependencyResolutionManagement {
    versionCatalogs {
        create("spring") {
            version("spring", "6.1.11")
            version("boot", "3.3.1")
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
        create("aws") {
            version("awssdk", "2.25.60")
            library("netty", "software.amazon.awssdk", "netty-nio-client").versionRef("awssdk")
            library("s3", "software.amazon.awssdk", "s3").versionRef("awssdk")

        }
        create("testcontainers") {
            version("testcontainers", "1.20.0")
            library("junit5", "org.testcontainers", "junit-jupiter").versionRef("testcontainers")
            library("core", "org.testcontainers", "testcontainers").versionRef("testcontainers")
            library("localstack", "org.testcontainers", "localstack").versionRef("testcontainers")
        }
    }
}
