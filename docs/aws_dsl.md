---
layout: default
title: AWS DSL
nav_order: 6
has_children: false
parent: Built-in DSLs
---

# AWS DSL
The AWS DSL makes it easy to create and configure AWS SDK clients. It exists here mostly to serve as an example of how a more complex DSL/Initializer can be implemented. It does not make use of runtime configuration as the clients are created only at startup, but it does show how to create multiple beans based on DSL usage, and allow code defaults to be set in the DSL as well as configuration overrides to be used from the environment.
These DSLs will automatically wire up the AWS SDK metrics to Micrometer, if your application has Micrometer configured.

## S3

If you have a class that uses S3 clients such as:
```kotlin
class MyLogic(val client1: S3AsyncClient, val client2: S3AsyncClient)
```

You can set up clients for injection using the DSL:
```kotlin
@SpringBootConfiguration
open class TestKotlinApplication : SpringDslApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        aws {
            s3 {
                client("client1") {
                    defaults {
                        // allows overriding the code defaults in S3ConfigurationProperties
                        maxConcurrency = 3
                    }
                    overrideConfig {
                        
                    }
                    customization {
                        
                    }
                }
                client("client2")
            }
        }
        beans {
            bean<MyLogic>{ MyLogic(ref("s3-client1"), ref("s3-client2")) }
        }
    }
}
```

Clients can be configured with other property sources:
```properties
awssdk.s3.client1.maxConcurrency=2
```
These settings will override the DSL defaults.

### Testing
Testing S3 clients can be done by using the Localstack testcontainer, and setting a global override to point the clients to Localstack:
```kotlin
@Testcontainers
@SpringBootTest(classes = [MyApplication::class])
class IntegrationTest {
    companion object {
        @Container
        val localStackContainer: LocalStackContainer =
            LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
                .withServices(LocalStackContainer.Service.S3)

        @DynamicPropertySource
        @JvmStatic
        fun dynamicConfig(registry: DynamicPropertyRegistry) {
            registry.add("awssdk.global.s3EndpointOverride") { localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3).toString() }
        }
    }
}
```