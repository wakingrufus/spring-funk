package com.github.wakingrufus.funk.htmx

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.servlet.function.ServerRequest

import java.nio.charset.StandardCharsets
import java.util.*

@JvmRecord
data class TestRecord(val name: String)
internal class DeserializerTest {

    @Test
    fun `test form`() {
        val objectMapper = ObjectMapper()
        objectMapper.registerModules(Jdk8Module() )
        val request = MockHttpServletRequest()
        request.addParameter("name", "test")
        request.contentType = MediaType.TEXT_HTML_VALUE
        val actual = ObjectMapper().deserializeForm<TestRecord>(
            ServerRequest.create(request, listOf(MappingJackson2HttpMessageConverter(), FormHttpMessageConverter()))
        )
        assertThat(actual).isEqualTo(TestRecord(name = "test"))
    }

    @Test
    fun `test json`() {
        val request = MockHttpServletRequest()
        request.setContent("""{"name": "test"}""".toByteArray(StandardCharsets.UTF_8))
        request.contentType = "application/json"
        val actual = deserializeJson<TestRecord>(
            ServerRequest.create(request, listOf(MappingJackson2HttpMessageConverter()))
        )
        assertThat(actual).isEqualTo(TestRecord(name = "test"))
    }
}