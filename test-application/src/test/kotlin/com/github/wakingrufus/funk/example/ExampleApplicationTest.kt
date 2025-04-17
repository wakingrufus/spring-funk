package com.github.wakingrufus.funk.example

import io.github.oshai.kotlinlogging.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.nio.charset.StandardCharsets


@SpringBootTest(
    classes = [ExampleApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["logging.level.com.github.wakingrufus.funk=DEBUG"]
)
@AutoConfigureWebTestClient
class ExampleApplicationTest {

    private val log = KotlinLogging.logger {}

    @Autowired
    lateinit var client: WebTestClient

    @Test
    fun test() {
        val response = client.get()
            .uri("/dsl")
            .header(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE)
            .exchange()
        response.expectStatus().isEqualTo(200)
    }

    @Test
    fun `test htmx`() {
        val response = client.get()
            .uri("/index")
            .accept(MediaType.TEXT_HTML)
            .exchange()
        log.info { response.returnResult(String::class.java).responseBodyContent?.toString(StandardCharsets.UTF_8) }

        val formResponse = client.post().uri("/load")
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("name", "bob"))
            .exchange()
        log.info { formResponse.returnResult(String::class.java).responseBodyContent?.toString(StandardCharsets.UTF_8) }

        val formResponseJson = client.post().uri("/load")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(HelloWorldRequest("bob")))
            .exchange()
        val responseString =
            formResponseJson.returnResult(String::class.java).responseBodyContent?.toString(StandardCharsets.UTF_8)
        log.info { responseString }
        assertThat(responseString).isEqualTo("""{"message":"Hello bob"}""")
    }
}