---
layout: default
title: Testing Custom DSLs
parent: Custom DSLs
nav_order: 4
---
# Testing Custom DSLs

Custom DSLs can be used in Test Spring Boot applications and tested via `@SpringBootTest`, but spring-funk-test provides a test fixture that facilitates faster tests which can share a test class. To use it, use the `testDslApplication` function. This function takes a list of initializers to run. Pass your initializer you want to test in here.

### Example
```kotlin
testDslApplication(CustomDsl()) {
    
}
```

The second argument is a DSL entry point. Within this DSL, you can use `application {}` to use your DSL and other DSLs. `environment {}` allows you to set environment properties. `test {}` allows you to access the resulting `ApplicationContext`. Your assertions should go there.
### Full Example
```kotlin
testDslApplication(CustomDsl()) {
    application {
        customDsl {
        }
    }
    environment {
        setProperty("prefix.property", "value")
    }
    test {
        assertThat(getBean<MyBean>().customProp).isEqualTo("value")
    }
}
```

### Initializer Dependencies

TODO