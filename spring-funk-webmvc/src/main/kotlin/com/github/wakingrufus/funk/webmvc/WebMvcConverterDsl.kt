package com.github.wakingrufus.funk.webmvc

import com.github.wakingrufus.funk.core.SpringDslMarker
import org.springframework.boot.autoconfigure.jackson.JacksonProperties

class WebMvcConverterDsl {
    internal var converters = mutableListOf<Converter>()
    internal var jacksonProperties: JacksonProperties? = null

    /**
     * Enable [org.springframework.http.converter.StringHttpMessageConverter]
     */
    @SpringDslMarker
    fun string() {
        converters.add(STRING)
    }

    /**
     * Enable [org.springframework.http.converter.ResourceHttpMessageConverter] and [org.springframework.http.converter.ResourceRegionHttpMessageConverter]
     */
    @SpringDslMarker
    fun resource() {
        converters.add(RESOURCE)
    }

    /**
     * Register an `ObjectMapper` bean and configure a [Jackson](https://github.com/FasterXML/jackson)
     * JSON converter on WebMvc server via a [dedicated DSL][JacksonDsl].
     *
     * Required dependencies can be retrieve using `org.springframework.boot:spring-boot-starter-json`
     * (included by default in `spring-boot-starter-webflux`).
     */
    @SpringDslMarker
    fun jackson(dsl: JacksonProperties.() -> Unit = {}) {
        converters.add(JACKSON)
        jacksonProperties = JacksonProperties().apply(dsl)
    }

    /**
     * Enable [org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter]
     */
    @SpringDslMarker
    fun form() {
        converters.add(FORM)
    }

    /**
     * Enable [org.springframework.http.converter.feed.AtomFeedHttpMessageConverter]
     */
    @SpringDslMarker
    fun atom() {
        converters.add(ATOM)
    }

    /**
     * Enable [org.springframework.http.converter.feed.RssChannelHttpMessageConverter]
     */
    @SpringDslMarker
    fun rss() {
        converters.add(RSS)
    }

    /**
     * Enable [org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter]
     */
    @SpringDslMarker
    fun kotlinSerialization() {
        converters.add(KOTLIN)
    }

    @SpringDslMarker
    fun custom(converter: Converter) {
        converters.add(converter)
    }
}