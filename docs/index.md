---
layout: default
title: Spring DSL
nav_order: 1
has_children: false
---
# Spring DSL

A framework for declarative DSL configuration for Spring Boot.

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
