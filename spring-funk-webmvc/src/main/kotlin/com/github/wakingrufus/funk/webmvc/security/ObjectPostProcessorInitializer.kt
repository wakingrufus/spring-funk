package com.github.wakingrufus.funk.webmvc.security

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.security.config.annotation.ObjectPostProcessor
import org.springframework.security.config.annotation.configuration.ObjectPostProcessorConfiguration
import java.util.function.Supplier


/**
 * [ApplicationContextInitializer] adapter for [ObjectPostProcessorConfiguration].
 */
class ObjectPostProcessorInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) {
        val configuration = ObjectPostProcessorConfiguration()
        context.registerBean(
            ObjectPostProcessor::class.java,
            Supplier { configuration.objectPostProcessor(context.beanFactory) })
    }
}