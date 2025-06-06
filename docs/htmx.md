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

You must declare the "landing page" for each page, which is the initial content to show on load. Use the HTML DSL to define that view.

```kotlin
page("/index") {
    div {
        span { +"Welcome to the Page" }
        button {
            +"Go"
        }
    }
}
```

### Templates
Templates can be declared which define how a given binding response should be rendered to htmx.

```kotlin
val myTemplate = htmxTemplate<ResponseDataClass> {
    div {
        span {
            +it.name
        }
    }
}
```

Templates can be composed.

```kotlin
val itemTemplate = htmxTemplate<String> {
    li { +it }
}
val listTemplate = htmxTemplate<List<String>> {
    ul {
        it.forEach {
            template(itemTemplate, it)
        }
    }
}
```

### HTMX Interaction

This library provides extensions on the HTML DSL in order to declare HTMX interaction.

```kotlin
page("/index") {
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
```

Each interaction which binds to a backend route will need a route declared to handle the request.

### HTMX routes

Routes are server-side handers for handling HTMX interactions. They consist of 4 main components:

1. HttpVerb: the HTTP verb/method to listen for (eg, GET, POST, etc).
2. route: the url path
3. handler: a function reference to call. This method must take a single parameter. The incoming request from HTMX will be deserialized into this parameter, then passed to the function.
4. renderer: a template for rendering the result of the handler call in HTMX. renderers can be templates, or defined inline.

Each route can be invoked via an HTMX trigger, which will result in the renderer being invoked, and HTML returned. However, these routes will also support returning the JSON of the data object which would be passed to the template via the "application/json" accept header.

#### Example route (inline)
```kotlin
route(HttpVerb.POST, helloWorldUrl, ExampleService::sayHello) {
    div {
        span {
            +it.message
        }
    }
}
```

#### Example route (template)
```kotlin
val myTemplate = htmxTemplate<ResponseDataClass> {
    div {
        span {
            +it.message
        }
    }
}
route(HttpVerb.POST, helloWorldUrl, ExampleService::sayHello, myTemplate)
```

#### GET
GET requests may have a parameter or no parameter.

##### GET requests with no parameter
```kotlin
get("things", TestController::getAll) {
    span {
        +it.id.toString()
    }
}
```

##### GET requests with a parameter
```kotlin
route(HttpVerb.GET, "thing/1", TestController::get) {
    span {
        +it.id.toString() 
    }
}
```