package com.github.wakingrufus.funk.webmvc

import com.github.wakingrufus.funk.core.SpringDsl
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.core.SpringDslMarker
import com.github.wakingrufus.funk.webmvc.security.WebMvcSecurityDsl

@SpringDslMarker
class WebmvcDsl : SpringDsl {
    internal var routes: RoutesDsl? = null
    internal var enableWebmvcDsl: EnableWebMvcDsl? = null
    internal var converterDsl: WebMvcConverterDsl? = null
    internal var securityDsl: WebMvcSecurityDsl? = null

    /**
     * Use this only if you are not using [org.springframework.web.servlet.config.annotation.EnableWebMvc] via autoconfiguration
     */
    @SpringDslMarker
    fun enableWebMvc(config: EnableWebMvcDsl.() -> Unit) {
        enableWebmvcDsl = EnableWebMvcDsl().apply(config)
    }

    @SpringDslMarker
    fun routes(config: RoutesDsl.() -> Unit) {
        routes = RoutesDsl().apply(config)
    }

    /**
     * Configure spring-security.
     *
     * Requires `org.springframework.boot:spring-boot-starter-security` dependency.
     *
     * @see WebMvcSecurityDsl
     */
    @SpringDslMarker
    fun security(config: WebMvcSecurityDsl.() -> Unit) {
        securityDsl = WebMvcSecurityDsl().apply(config)
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
