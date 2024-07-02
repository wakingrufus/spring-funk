package com.github.wakingrufus.funk.webmvc

import org.springframework.beans.factory.getBeanProvider
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean

class JettyEngine : ServletEngine {
    override fun initialize(serverProperties: ServerProperties, context: GenericApplicationContext) {
        context.registerBean<ConfigurableServletWebServerFactory> {
            val serverCustomizers = context.getBeanProvider<JettyServerCustomizer>()
            val factory = JettyServletWebServerFactory()
            factory.serverCustomizers.addAll(serverCustomizers.orderedStream().toList())
            factory
        }
    }
}