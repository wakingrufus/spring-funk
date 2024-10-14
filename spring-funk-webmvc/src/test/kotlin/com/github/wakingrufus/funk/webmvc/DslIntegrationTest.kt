package com.github.wakingrufus.funk.webmvc

import com.github.wakingrufus.funk.base.SpringFunkApplication
import com.github.wakingrufus.funk.beans.beans
import com.github.wakingrufus.funk.core.SpringDslContainer
import io.github.oshai.kotlinlogging.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router
import java.net.URI

@SpringBootTest(
    classes = [FunkApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ContextConfiguration(initializers = [FunkApplication::class])
internal class DslIntegrationTest {
    private val log = KotlinLogging.logger {}

    @Autowired
    lateinit var client: TestRestTemplate

    @Autowired
    lateinit var context: ApplicationContext

    @Test
    fun test() {
        val response = client.getForEntity<String>(URI.create("/dsl"))
        assertThat(response.statusCode.value()).isEqualTo(200)
        val response2 = client.getForEntity<String>(URI.create("/dsl2"))
        assertThat(response2.statusCode.value()).isEqualTo(200)
        context.beanDefinitionNames.forEach { log.info { it } }
    }

}

internal class FunkApplication : SpringFunkApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        beans {
            bean<ServiceClass>()
        }
        webmvc {
            enableWebMvc {
                jetty()
            }

            routes {
                router { serviceClass: ServiceClass -> {
                        GET("/dsl2", serviceClass::get)
                    }
                }
                router {
                    helloWorldApi(ref())
                }
            }
        }
    }
}

class ServiceClass {
    fun get(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().body("Hello, World")
    }
}

fun helloWorldApi(serviceClass: ServiceClass) = router {
    GET("/dsl", serviceClass::get)
}
