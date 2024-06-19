---
layout: default
title: Testing Applications
nav_order: 4
---
# Testing Applications
Spring DSL applications can be tested using `@SpringBootTest` just like a normal Spring Boot application.

### Example
#### Application
```kotlin
class DslApplication : SpringFunkApplication {  
    override fun dsl(): SpringDslContainer.() -> Unit = {  
        webmvc {  
            enableWebMvc {  
                jetty()  
            }  
            router {  
                GET("/dsl") {  
                    ServerResponse.ok().build()  
                }  
            }
        }    
    }
}
```
#### Test
```kotlin
@SpringBootTest(  
    classes = [DslApplication::class],  
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT  
)
internal class DslIntegrationTest {  
    @Autowired  
    lateinit var client: TestRestTemplate  
  
    @Autowired  
    lateinit var context: ApplicationContext  
  
    @Test  
    fun test() {  
        val response = client.getForEntity<String>(URI.create("/dsl"))  
        assertThat(response.statusCode.value()).isEqualTo(200)
    }  
}  
```
