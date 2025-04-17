---
layout: default
title: HTMX
nav_order: 9
has_children: false
parent: Built-in DSLs
---

# HTMX DSL

This DSL allows you to seamlessly integrate [HTMX](https://htmx.org) with the Kotlin HTML DSL.

## Usage

```kotlin
const val helloWorldUrl = "/load"
class TestKotlinApplication : SpringFunkApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        htmx {
            page("/index") {
                route(HttpVerb.POST, helloWorldUrl, ExampleService::sayHello) {
                    {
                        div {
                            span {
                                +it.message
                            }
                        }
                    }
                }
                initialLoad {
                    form {
                        hxPost(helloWorldUrl) {
                            swap(HxSwapType.OuterHtml) {
                            }
                        }
                        textInput(name = "name") {
                        }
                        button(classes = "btn primary") {
                            +"Submit"
                        }
                    }
                    button {
                        hxPost(helloWorldUrl) {
                            swap(HxSwapType.OuterHtml)
                        }
                        +"Click Me"
                    }
                }
            }
        }
    }
}
```
