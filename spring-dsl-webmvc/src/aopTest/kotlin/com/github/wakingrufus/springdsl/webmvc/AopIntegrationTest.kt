package com.github.wakingrufus.springdsl.webmvc

import io.github.oshai.kotlinlogging.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.context.ApplicationContext
import java.net.URI

@SpringBootTest(
    classes = [AopApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class AopIntegrationTest {
    private val log = KotlinLogging.logger {}

    @Autowired
    lateinit var client: TestRestTemplate

    @Autowired
    lateinit var context: ApplicationContext

    @Test
    fun test() {
        val response = client.getForEntity<String>(URI.create("/controller"))
        assertThat(response.statusCode.value()).isEqualTo(200)
        context.beanDefinitionNames.forEach { log.info { it } }
    }
}