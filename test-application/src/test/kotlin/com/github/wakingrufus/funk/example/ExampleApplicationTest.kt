package com.github.wakingrufus.funk.example

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import java.net.URI

@SpringBootTest(
    classes = [ExampleApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["logging.level.com.github.wakingrufus.funk=DEBUG"]
)
class ExampleApplicationTest {

    @Autowired
    lateinit var client: TestRestTemplate

    @Test
    fun test() {
        val response = client.getForEntity<String>(URI.create("/dsl"))
        assertThat(response.statusCode.value()).isEqualTo(200)
    }
}