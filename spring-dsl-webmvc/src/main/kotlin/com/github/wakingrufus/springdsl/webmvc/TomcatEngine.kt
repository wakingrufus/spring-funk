package com.github.wakingrufus.springdsl.webmvc

import org.springframework.beans.factory.getBeanProvider
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.autoconfigure.web.servlet.TomcatServletWebServerFactoryCustomizer
import org.springframework.boot.autoconfigure.web.servlet.registerTomcatHeaderForwarder
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean

class TomcatEngine : ServletEngine {

    override fun initialize(serverProperties: ServerProperties, context: GenericApplicationContext) {
        context.registerBean<TomcatServletWebServerFactory> {
            val connectorCustomizers = context.getBeanProvider<TomcatConnectorCustomizer>()
            val contextCustomizers = context.getBeanProvider<TomcatContextCustomizer>()
            val protocolHandlerCustomizers = context.getBeanProvider<TomcatProtocolHandlerCustomizer<*>>()
            val factory = TomcatServletWebServerFactory()
            factory.tomcatConnectorCustomizers.addAll(connectorCustomizers.orderedStream().toList())
            factory.tomcatContextCustomizers.addAll(contextCustomizers.orderedStream().toList())
            factory.tomcatProtocolHandlerCustomizers.addAll(protocolHandlerCustomizers.orderedStream().toList())
            factory
        }
        context.registerBean<TomcatServletWebServerFactoryCustomizer> {
            TomcatServletWebServerFactoryCustomizer(serverProperties)
        }
        registerTomcatHeaderForwarder(context, serverProperties)
        context.registerBean<ConfigurableServletWebServerFactory> {
            TomcatServletWebServerFactory()
        }
    }
}