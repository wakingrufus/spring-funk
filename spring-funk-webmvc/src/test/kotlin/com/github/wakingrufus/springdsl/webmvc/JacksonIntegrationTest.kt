package com.github.wakingrufus.springdsl.webmvc

import com.github.wakingrufus.springdsl.base.SpringFunkApplication
import com.github.wakingrufus.funk.core.SpringDslContainer
import io.github.oshai.kotlinlogging.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.servlet.function.ServerResponse
import java.net.URI

@SpringBootTest(
    classes = [JacksonFunkApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@ContextConfiguration(initializers = [JacksonFunkApplication::class])
internal class JacksonIntegrationTest {
    private val log = KotlinLogging.logger {}

    @Autowired
    lateinit var client: TestRestTemplate

    @Test
    fun test() {
        val response = client.getForEntity<Dto>(URI.create("/dsl"))
        assertThat(response.statusCode.value()).isEqualTo(200)
        assertThat(response.body).isEqualTo(Dto("Hello World"))
    }
}

internal data class Dto(val property: String)
internal class JacksonFunkApplication : SpringFunkApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        webmvc {
            enableWebMvc {
                jetty()
            }
            converters {
                jackson()
            }
            router {
                GET("/dsl") {
                    ServerResponse.ok().body(Dto("Hello World"))
                }
            }
        }
    }
}