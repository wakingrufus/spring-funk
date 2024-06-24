package com.github.wakingrufus.funk

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.core.env.Environment

class SpringRuntimeConfig<T : Any>(
    private val clazz: Class<T>,
    private val environment: Environment,
    internal val prefix: String,
    private val initialInstance: T? = null
) : RuntimeConfig<T> {

    companion object {
        fun <T : Any> bind(
            clazz: Class<T>,
            environment: Environment,
            prefix: String? = null,
            initialInstance: T? = null
        ): SpringRuntimeConfig<T> {
            val resolvedPrefix = prefix ?: clazz.getAnnotationsByType(ConfigurationProperties::class.java)
                .firstNotNullOfOrNull { it.prefix } ?: ""
            return SpringRuntimeConfig(clazz, environment, resolvedPrefix, initialInstance)
        }

        inline fun <reified T : Any> bind(
            environment: Environment,
            prefix: String? = null,
            initialInstance: T? = null
        ): SpringRuntimeConfig<T> {
            return bind(T::class.java, environment, prefix, initialInstance)
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
        instance = if (initialInstance != null) {
            Binder.get(environment).bind<T>(prefix, Bindable.ofInstance(initialInstance))
                .orElse(initialInstance)
        } else {
            Binder.get(environment).bindOrCreate(prefix, clazz)
        }
    }
}
