---
layout: default
title: Writing a DSL
nav_order: 1
parent: Custom DSLs
---

# Writing a DSL

A Kotlin DSL is really just a [type-safe builder](https://kotlinlang.org/docs/type-safe-builders.html) for a data structure. To extend the Spring DSL framework to add your own DSL, first define you main data class. This class must implement the `SpringDsl` interface. It should be annotated with `@SpringDslMarker`.

```kotlin
@SpringDslMarker
class CustomDsl : SpringDsl 
```

The simplest way to add a property to the DSL is by exposing a mutable property.

```kotlin
@SpringDslMarker
class CustomDsl : SpringDsl {
    @SpringDslMarker
    var aProperty: String? = null
}
```

More complex behavior can be driven by functions. These functions should be annotated with `@SpringDslMarker`. Properties not meant to be accessed by the DSL should not be annotated and should have `internal` visibility.

```kotlin
@SpringDslMarker
class CustomDsl : SpringDsl {
    internal var implementation: MyInterface? = null

    @SpringDslMarker
    fun impl1() {
        implementation = Impl1()
    }

    @SpringDslMarker
    fun impl2() {
        implementation = Impl2()
    }
}
```

## Nested DSLs
You can also nest DSL objects. Nested DSLs do not need to implement `SpringDsl` but should be annotated with `@SpringDslMarker`.

```kotlin
@SpringDslMarker
class CustomDsl : SpringDsl {
    internal val nestedDsl: NestedDsl = NestedDsl()

    @SpringDslMarker
    fun nested(builder: NestedDsl.() -> Unit) { 
        nestedDsl.apply(builder)
    }
}
```

## Entry point

In order to make your DSL accessible from the root-level Spring DSL, add an extension method as an entry point which calls the `register` method on `SpringDslContainer`.

```kotlin
fun SpringDslContainer.customDsl(builder: CustomDsl.() -> Unit) {
    register(CustomDsl().apply(builder))
}
```
Next, you will use this DSL to drive behavior in your [initializer](writing_initializers.md).