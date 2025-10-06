package com.github.wakingrufus.funk.webmvc

import com.github.wakingrufus.funk.base.SpringFunkApplication
import com.github.wakingrufus.funk.beans.beans
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.logging.logging
import io.github.oshai.kotlinlogging.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.logging.LogLevel
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.test.client.TestRestTemplate
import org.springframework.boot.web.server.test.client.getForEntity
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
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

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableWebMvc
open class FunkApplication : SpringFunkApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        beans {
            registerBean<ServiceClass>()
        }
        logging {
            level("org.springframework.web", LogLevel.DEBUG)
        }
        webmvc {
            router { serviceClass: ServiceClass ->
                {
                    GET("/dsl", serviceClass::get)
                    GET("/dsl2", serviceClass::get)
                }
            }
            addRouter(dslRoute)
        }
    }
}

class ServiceClass {
    fun get(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().body("Hello, World")
    }
}

val dslRoute = router { bean: ServiceClass ->
    {
        GET("/dsl", bean::get)
    }
}
