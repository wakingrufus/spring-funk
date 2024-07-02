package com.github.wakingrufus.funk.webmvc

import org.springframework.beans.factory.getBeanProvider
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.autoconfigure.web.servlet.UndertowServletWebServerFactoryCustomizer
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean

class UndertowEngine : ServletEngine {
    override fun initialize(serverProperties: ServerProperties, context: GenericApplicationContext) {
        context.registerBean<UndertowServletWebServerFactory> {
            val deploymentInfoCustomizers = context.getBeanProvider<UndertowDeploymentInfoCustomizer>()
            val builderCustomizers = context.getBeanProvider<UndertowBuilderCustomizer>()
            val factory = UndertowServletWebServerFactory()
            factory.deploymentInfoCustomizers.addAll(deploymentInfoCustomizers.orderedStream().toList())
            factory.builderCustomizers.addAll(builderCustomizers.orderedStream().toList())
            factory
        }
        context.registerBean<UndertowServletWebServerFactoryCustomizer> {
            UndertowServletWebServerFactoryCustomizer(serverProperties)
        }
        context.registerBean<ConfigurableServletWebServerFactory> {
            UndertowServletWebServerFactory()
        }

    }
}