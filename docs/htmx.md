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

The HTMX DSL requires webmvc. You can set this up via:

```kotlin
class TestKotlinApplication : SpringFunkApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        webmvc {
            enableWebMvc {
                jetty()
            }
            converters {
                jackson()
                form()
                string()
            }
        }
    }
}
```

### Page

Each discrete "page" or "SPA" you would like to build starts with an HTMX "page". These are declared in the DSL with the url route to the page you would like to use.

```kotlin

class TestKotlinApplication : SpringFunkApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        htmx {
            page("/index") {
            }
        }
    }
}
```

### Initial Load

You must declare the "landing page" for each page, which is the initial content to show on load. The `initialLoad` function lets you do this, which exposes the HTML DSL to define that view.

```kotlin
page("/index") {
    initialLoad {
        div {
            span { +"Welcome to the Page" }
            button {
                +"Go"
            }
        }
    }
}
```

### HTMX Interaction

This library provides extensions on the HTML DSL in order to declare HTMX interaction.

```kotlin
page("/index") {
    initialLoad {
        div {
            span { +"Welcome to the Page" }
            button {
                hxGet("/start") {
                    swap(HxSwapType.OuterHtml)
                }
                +"Go"
            }
        }
    }
}
```

Each interaction which binds to a backend route will need a route declared to handle the request.

### HTMX routes

#### GET
GET requests may have a parameter or no parameter.

##### GET requests with no parameter
```kotlin
page("/index") {
    get("things", TestController::getAll) {
        {
            span {
                +it.id.toString()
            }
        }
    }
}
```

##### GET requests with a parameter
```kotlin
page("/index") {
    route(HttpVerb.GET, "thing/1", TestController::get) {
        {
            span {
                +it.id.toString()
            }
        }
    }
}
```