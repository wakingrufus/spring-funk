package com.github.wakingrufus.springdsl.webmvc

import com.github.wakingrufus.springdsl.base.SpringDslApplication
import com.github.wakingrufus.springdsl.core.SpringDslContainer
import io.github.oshai.kotlinlogging.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.servlet.function.ServerResponse
import java.net.URI

@SpringBootTest(
    classes = [DslApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ContextConfiguration(initializers = [DslApplication::class])
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
        context.beanDefinitionNames.forEach { log.info { it } }
    }
}

internal class DslApplication : SpringDslApplication {
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
