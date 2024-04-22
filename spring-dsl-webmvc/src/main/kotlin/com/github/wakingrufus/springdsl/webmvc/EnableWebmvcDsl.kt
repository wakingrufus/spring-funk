package com.github.wakingrufus.springdsl.webmvc

import com.github.wakingrufus.springdsl.core.SpringDslMarker

class EnableWebMvcDsl {
    internal var engine: ServletEngine? = null

    @SpringDslMarker
    fun tomcat() {
        engine = TomcatEngine()
    }

    @SpringDslMarker
    fun jetty() {
        engine = JettyEngine()
    }

    @SpringDslMarker
    fun undertow() {
        engine = UndertowEngine()
    }

    @SpringDslMarker
    fun custom(engine: ServletEngine) {
        this.engine = engine
    }
}