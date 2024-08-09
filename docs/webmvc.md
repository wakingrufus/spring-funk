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
open class TestKotlinApplication : SpringFunkApplication {
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

#### Example
```kotlin
open class TestKotlinApplication : SpringFunkApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        webmvc {
            routes {
                route {
                    GET("/dsl") {
                        ServerResponse.ok().body(Dto("Hello World"))
                    }
                }
            }
        }
    }
}
```

### Routers with Bean Injection

In order to use bean injection in your routers, declare a separate router function.
Then register this function using `ref()` to inject, similar to the beans DSL.

#### Example
```kotlin
fun helloWorldApi(serviceClass: ServiceClass) = router {
    GET("/hello", serviceClass::get)
}
open class TestKotlinApplication : SpringFunkApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        webmvc {
            routes {
                router {
                    helloWorldApi(ref())
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
open class TestKotlinApplication : SpringFunkApplication {
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