---
layout: default
title: Webmvc DSL
nav_order: 6
has_children: false
parent: Built-in DSLs
---

# WebMvc
This DSL allows you to enable and configure WebMvc applications.

## enableWebMvc
This DSL allows you to enable webmvc. This is alternative to the `@EnableWebMvc` autoconfiguration annotation.
If you do use the annotation, do not use this part of the webmvc DSL, but you may still use the other parts of the webmvc DSL to configure webmvc. Within this DSL, you may configure your servlet container: jetty, tomcat, undertow, or custom.
### Example
```kotlin
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

## Router DSL
The Router DSL exposes the Spring built-in `RouterFunctionDsl`.

### Example
```kotlin
open class TestKotlinApplication : SpringDslApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        webmvc {
            router {
                GET("/dsl") {
                    ServerResponse.ok().body(Dto("Hello World"))
                }
            }
        }
    }
}
```

## Converters
The Converters DSL allows you to set up message converters. There is built-in support for String, RSS, Atom, Jackson, kotlinx.serialization, form, resource, and custom.

### Example
```kotlin
open class TestKotlinApplication : SpringDslApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        webmvc {
            converters {
                string()
                jackson()
            }
        }
    }
}
```