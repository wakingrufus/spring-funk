---
layout: default
title: Runtime Configuration Properties
nav_order: 7
has_children: false
parent: Built-in DSLs
---

# Runtime Configuration Properties DSL

This DSL allows you to bind ConfigurationProperties which can receive runtime updates.

## Why?
Spring Cloud Context supports updating environment properties at runtime via EnvironmentManager. ConfigurationProperties will receive these updates. However, this does not work with immutable ConfigurationProperties classes. However, exposing setters on ConfigurationProperties classes can give the wrong impression to developers that they may call the setter, and that will propagate runtime configuration changes back up to the Environment. This DSL promotes a different approach where ConfigurationPropoerties classes may be immutable, and accessed though a container which is similar to a Supplier interface. 

## Usage

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
class TestKotlinApplication : SpringFunkApplication {
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
