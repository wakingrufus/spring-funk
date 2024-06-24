package com.github.wakingrufus.funk.config

import com.github.wakingrufus.funk.core.SpringDsl
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.core.SpringDslMarker

/**
 * A DSL for declaring configuration objects which can be updated at runtime
 * In Spring, this replaces `@EnableConfigurationProperties`
 */
@SpringDslMarker
class RuntimeConfigDsl : SpringDsl {

    internal val configClasses: MutableList<ConfigClassEntry<out Any>> = ArrayList()

    fun <T : Any> add(binding: ConfigClassEntry<T>) {
        configClasses.add(binding)
    }

    @SpringDslMarker
    fun <T : Any> RuntimeConfigDsl.registerConfigClass(clazz: Class<T>) {
        add(ConfigClassEntry(clazz, null))
    }

    @SpringDslMarker
    fun <T : Any> RuntimeConfigDsl.registerConfigClass(prefix: String, clazz: Class<T>) {
        add(ConfigClassEntry(clazz, prefix))
    }

    @SpringDslMarker
    inline fun <reified T : Any> RuntimeConfigDsl.registerConfigClass() {
        add(ConfigClassEntry(T::class.java, null))
    }

    @SpringDslMarker
    inline fun <reified T : Any> RuntimeConfigDsl.registerConfigClass(prefix: String) {
        add(ConfigClassEntry(T::class.java, prefix))
    }
}

@SpringDslMarker
fun SpringDslContainer.runtimeConfig(config: RuntimeConfigDsl.() -> Unit) {
    register(RuntimeConfigDsl().apply(config))
}
