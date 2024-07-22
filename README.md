# Spring FunK

A framework for declarative DSL configuration for Spring Boot.

[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.wakingrufus/spring-funk-core?style=for-the-badge&color=01AF01)](https://repo1.maven.org/maven2/io/github/wakingrufus/)

## Introduction

This project delivers an API for accessing Spring's beans DSL and router DSL as well as some additional DSLs that don't exist in core Spring. 
Spring FunK is also designed for extension. This should make it ideal for platform engineers who are building on top of Spring Boot.
Spring FunK will work with AutoConfigured Spring boot applications, but if your application is not using AOP at all (AutoConfiguration, Component Scanning, Spring annotations), it will also work with these features disabled in Spring, which will allow you to unlock very fast start-up times, similar to [spring-fu](https://github.com/spring-projects-experimental/spring-fu).
For more background, see the [Documentation](https://wakingrufus.github.io/spring-funk/introduction.html).

The TL;DR is that this library lets you write a Spring Boot application that looks like this:
```kotlin
class ExampleApplication : SpringFunkApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        webmvc {
            enableWebMvc {
                jetty()
            }
            router {
                GET("/dsl") {
                    ServerResponse.ok().body("Hello World")
                }
            }
        }
    }
}
```

## Contributing

In order to build the project, clone the repository and run `./gradlew build`.
Contributions are very welcome for:
- Documentation
- Adding a module which will convert an existing Spring Boot AutoConfiguration into an ApplicationContextInitializer
- Adding or modifying a module which will expose more Spring Boot configuration via the DSL.

Other contributions may be welcome. Start by opening an [issue](https://github.com/wakingrufus/spring-funk/issues).
