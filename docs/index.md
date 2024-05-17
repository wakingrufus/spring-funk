---
layout: default
title: Spring DSL
nav_order: 1
has_children: false
---
# Spring DSL

This project is an attempt to build on the concepts pioneered by Sébastien Deleuze in [spring-fu](https://github.com/spring-projects-experimental/spring-fu), but with is a slightly different focus.

### What is Spring-Fu?

Spring Fu is a declarative way of configuring spring boot applications. Instead of using XML, or AOP (annotations), it uses Kotlin DSL builder patterns to deliver a developer experience akin to Micronaut or [Ktor](https://ktor.io/). There are 3 main advantages to this approach:

1) Performance: Because it does not use reflection, it has a much faster start time than an application using "AutoConfigure".
2) GraalVM compatibility:  Because it does not use reflection,it can run out-of-the-box on GraalVM (which gives even more startup performance gains).
3) Developer ergonomics: The functional DSL style lends itself much better to traditional IDE autocomplete, making applications easier to reason about than the traditional AOP/annotation approach, which is losing popularity sue to functional programming becoming more popular.

The major drawback to Spring-Fu, is that it is an "all-in" proposition. An application must be 100% using the framework, and if it does, it can't use any existing AutoConfiguration-based libraries.

The Spring team decided to focus on GraalVM compatibility for Spring Boot 3, and forcing a rewrite of every Spring Boot application was not a good path to get there, so the team pivoted and accomplished that goal in another way. This left Spring Fu on hold.

Some of the concepts from Spring-Fu have made their way into core Spring, such as beansDsl and routerDsl, and they can be optionally used today. But there is not a seamless way to integrate these into an existing application, and most of the rest of Spring-Fu is still not available in Spring Core.

### Goals of Spring DSL

This project aims to deliver an API for accessing the existing beansDsl and routerDsl for any Spring Boot application.

It will also provide some additional DSL that don't exist in core Spring.

Finally, it will also be designed for extension. This should make it ideal for Platform engineers who are building internal runtime frameworks on top of Spring Boot, and want to deliver the DX/ergonomics that come with it to their teams as well.

Spring DSL will work with AutoConfigured Spring boot applications, but if your application is not using AOP at all (AutoConfiguration, Component Scanning, Spring annotations), it will also work with these features disabled in Spring, which will allow you to unlock very fast start-up times, similar to Spring-Fu.


## Getting Started

Starting from a standard Spring Boot Application, add the following dependency to your project:

```kotlin
dependencies {
    implementation("com.github.wakingrufus.springdsl:spring-dsl-base")
}
```

Create a `src/main/resources/META-INF/spring.factories` file within your Spring Boot Application Project. Add an entry for your main Application class:

```
org.springframework.context.ApplicationContextInitializer=\
com.your.org.YourApplication
```

In your application class, implement the `SpringDslApplication` interface and the dsl method.

Example:
```kotlin
@SpringBootConfiguration
open class TestKotlinApplication : SpringDslApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
    }
}
```

Within this method, you will have access to the DSL API

## Built-in DSLs

### Beans DSL
The beans DSL gives access to the built-in Spring BeanDefinitionDsl.

### WebMvc
This DSL allows you to enable and configure WebMvc applications.

#### enableWebMvc
This DSL allows you to enable webmvc. This is alternative to the `@EnableWebMvc` autoconfiguration annotation.
If you do use the annotation, do not use this part of the webmvc DSL, but you may still use the other parts of the webmvc DSL to configure webmvc.
```kotlin
@SpringBootConfiguration
open class TestKotlinApplication : SpringDslApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        webmvc {
            enableWebMvc {
                jetty()
            }
        }
    }
}
```

#### Router DSL

### Runtime Configuration DSL

The Runtime Configuration DSL allows you to declare immutable `@ConfigurationProperties` classes which can
still receive runtime updates.

Declare an immutable configuration properties class:

```java
@Value
@ConstructorBinding
@AllArgsConstructor
@ConfigurationProperties(prefix = "prefix")
public class ConfigProps {
    @Nonnull
    String figKey;
}
```

Register it in the DSL:

```kotlin
@SpringBootConfiguration
open class TestKotlinApplication : SpringDslApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        runtimeConfig {
            registerConfigClass<ConfigProps>()
        }
    }
}
```

Inject it as a `RuntimeConfig` in order to access it:

```java
@Autowired RuntimeConfig<ConfigProps> config;
```


## Creating Custom DSLs
