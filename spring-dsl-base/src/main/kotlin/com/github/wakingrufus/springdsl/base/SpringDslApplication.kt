package com.github.wakingrufus.springdsl.base

import com.github.wakingrufus.springdsl.core.SpringDslContainer
import com.github.wakingrufus.springdsl.core.SpringDslMarker
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.Ordered

/**
 * Implement this interface on your Spring application class to access the Spring DSL by overriding [dsl].
 */
fun interface SpringDslApplication : Ordered, ApplicationContextInitializer<GenericApplicationContext> {

    override fun getOrder(): Int = Int.MIN_VALUE

    @SpringDslMarker
    fun dsl(): SpringDslContainer.() -> Unit

    fun configure(dsl: SpringDslContainer) {
        dsl().invoke(dsl)
    }

    override fun initialize(applicationContext: GenericApplicationContext) {
        val springDsl = SpringDslContainer()
        configure(springDsl)
        applicationContext.addApplicationListener(SpringDslListener(springDsl))
    }
}