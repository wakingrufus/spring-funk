package com.github.wakingrufus.springdsl.runtimeconfig

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.core.env.Environment

class SpringRuntimeConfig<T : Any>(
    private val clazz: Class<T>,
    private val environment: Environment,
    val prefix: String
) : RuntimeConfig<T> {

    companion object {
        fun <T : Any> create(clazz: Class<T>, environment: Environment, prefix: String? = null): SpringRuntimeConfig<T> {
            val resolvedPrefix = prefix ?: clazz.getAnnotationsByType(
                ConfigurationProperties::class.java
            ).firstNotNullOfOrNull { it.prefix } ?: ""
            return SpringRuntimeConfig(clazz, environment, resolvedPrefix)
        }
    }

    private lateinit var instance: T

    init {
        update()
    }

    override fun get(): T {
        return instance
    }

    fun update() {
        instance = Binder.get(environment).bindOrCreate(prefix, clazz)
    }
}
