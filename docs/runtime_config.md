---
layout: default
title: Runtime Configuration Properties
nav_order: 7
has_children: false
parent: Built-in DSLs
---

# Runtime Configuration Properties DSL

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
