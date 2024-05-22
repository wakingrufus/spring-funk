---
layout: default
title: Getting Started
nav_order: 3
has_children: false
---
# Getting Started

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

In your application class, implement the `SpringDslApplication` interface and the dsl method. Within this method, you will have access to the DSL API.

### Autoconfiguration Example
```kotlin
@SpringBootConfiguration  
@EnableAutoConfiguration
open class TestKotlinApplication : SpringDslApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
    }
}
```
### Pure DSL Example
In order to avoid Autoconfiguration, omit the Spring Boot annotations from your application class.
```kotlin
class TestKotlinApplication : SpringDslApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
    }
}
```