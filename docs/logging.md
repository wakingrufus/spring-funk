---
layout: default
title: Logging
nav_order: 8
has_children: false
parent: Built-in DSLs
---

# Logging DSL

This DSL allows you to configure logging levels. Setting levels with `logging.level` configuration properties will override the DSL setting.

## Usage

```kotlin
class TestKotlinApplication : SpringFunkApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        logging {
            root(LogLevel.INFO)
            level<MyClass>(LogLevel.OFF)
            level("com.example.package", LogLevel.DEBUG)
        }
    }
}
```
