package com.github.wakingrufus.funk.base

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext

/**
 * An example of a DSL-enabled ApplicationContextInitializer written in Kotlin
 */
internal class TestDslInitializer :
    ApplicationContextInitializer<GenericApplicationContext> {
    private val log = KotlinLogging.logger {}
    override fun initialize(applicationContext: GenericApplicationContext) {
        applicationContext.getDsl<TestDsl>()
            ?.stringBeans
            ?.forEach { (key, value) ->
                log.info { "registering $key" }
                applicationContext.registerBean(key, String::class.java, value)
            }
    }
}
