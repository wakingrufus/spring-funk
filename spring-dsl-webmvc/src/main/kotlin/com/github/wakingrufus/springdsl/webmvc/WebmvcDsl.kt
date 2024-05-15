package com.github.wakingrufus.springdsl.webmvc

import com.github.wakingrufus.springdsl.core.SpringDsl
import com.github.wakingrufus.springdsl.core.SpringDslContainer
import com.github.wakingrufus.springdsl.core.SpringDslMarker
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctionDsl
import org.springframework.web.servlet.function.ServerResponse

@SpringDslMarker
class WebmvcDsl : SpringDsl {
    internal var routerDsl: RouterFunction<ServerResponse>? = null
    internal var enableWebmvcDsl: EnableWebMvcDsl? = null
    internal var converterDsl: WebMvcConverterDsl? = null

    /**
     * Use this only if you are not using [org.springframework.web.servlet.config.annotation.EnableWebMvc] via autoconfiguration
     */
    @SpringDslMarker
    fun enableWebMvc(config: EnableWebMvcDsl.() -> Unit) {
        enableWebmvcDsl = EnableWebMvcDsl().apply(config)
    }

    @SpringDslMarker
    fun router(config: RouterFunctionDsl.() -> Unit) {
        routerDsl = org.springframework.web.servlet.function.router(config)
    }

    /**
     * Configure converters via a [dedicated DSL][WebMvcConverterDsl].
     */
    @SpringDslMarker
    fun converters(init: WebMvcConverterDsl.() -> Unit = {}) {
        converterDsl = WebMvcConverterDsl().apply(init)
    }
}

@SpringDslMarker
fun SpringDslContainer.webmvc(config: WebmvcDsl.() -> Unit) {
    register(WebmvcDsl().apply(config))
}
