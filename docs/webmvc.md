---
layout: default
title: Webmvc DSL
nav_order: 6
has_children: false
parent: Built-in DSLs
---

# WebMvc
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
