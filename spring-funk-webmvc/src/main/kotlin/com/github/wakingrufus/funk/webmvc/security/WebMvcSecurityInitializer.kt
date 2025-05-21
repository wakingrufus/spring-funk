package com.github.wakingrufus.funk.webmvc.security

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.security.config.annotation.web.servlet.configuration.WebMvcSecurityConfiguration
import org.springframework.web.servlet.support.RequestDataValueProcessor
import java.util.function.Supplier


/**
 * [ApplicationContextInitializer] adapter for [WebMvcSecurityConfiguration].
 */
class WebMvcSecurityInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) {
        val configurationSupplier =
            Supplier<WebMvcSecurityConfiguration> {
                val configuration = WebMvcSecurityConfiguration()
                configuration.setApplicationContext(context)
                configuration
            }

        context.registerBean(
            RequestDataValueProcessor::class.java,
            Supplier { configurationSupplier.get().requestDataValueProcessor() })
    }
}