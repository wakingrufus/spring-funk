---
layout: default
title: Writing an Initializer
nav_order: 2
parent: Custom DSLs
---
# Writing an Initializer

Initializers must implement `ApplicationContextInitializer<GenericApplicationContext>`. In the `initialze` method, you can access your DSL using the `getDsl` extension method.

```kotlin
class CustomInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        applicationContext.getDsl<CustomDsl>()?.run {
            // register beans here
        }
    }
}
```

## spring.factories

In order for your initializer to be auto-discovered by Spring Boot applications, you must add an entry in `src/main/resources/META-INF/spring.factories`.

```kotlin
org.springframework.context.ApplicationContextInitializer=\
com.yourorg.CustomDsl
```