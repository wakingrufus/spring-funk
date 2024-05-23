---
layout: default
title: Interacting with Configuration Properties
nav_order: 3
parent: Custom DSLs
---
# Interacting with Configuration Properties

If you would like to use ConfigurationProperties to configure the behavior of your DSL/Initializer, it is best practice to bind the ConfigurationProperties to your environment, but also expose the ConfigurationProperties to the DSL to allow programmatic default overrides. To do this, first add the ConfigurationProperties to your DSL.

### Example
```kotlin
class ExampleDsl(){
    var defaults: ExampleConfig = ExampleConfig()
    fun defaults(config: ExampleConfig.() -> Unit){
        defaults.apply(config)
    }
}

fun SpringDslContainer.exampleDsl(ExampleDsl.() -> Unit){
    register(ExampleDsl().apply(config))
}
```

Then, in your initializer, register the ConfigurationProperties using the helper provided by the spring-dsl-runtimeconfig package
### Example
```kotlin
applicationContext.registerRuntimeConfig<ExampleConfig>()
```
Then use the config in your bean using another helper from spring-dsl-runtimeconfig

### Example
```kotlin
applicationContext.registerBean<MyBean>{ MyBean(applicationContext.getRuntimeConfig<ExampleConfig>()) }
```

Following this method, the configuration properties will be consolidated such that properties from the Environment override the code defaults supplied in the DSL.