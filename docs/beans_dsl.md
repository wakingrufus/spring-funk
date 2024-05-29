---
layout: default
title: Beans DSL
nav_order: 5
has_children: false
parent: Built-in DSLs
---

# Beans DSL
The beans DSL gives access to the built-in Spring BeanDefinitionDsl.

Example:
```kotlin
@SpringBootConfiguration
open class TestKotlinApplication : SpringDslApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        beans {
            bean<MyBean>{ MyBean(ref()) }
        }
    }
}
```