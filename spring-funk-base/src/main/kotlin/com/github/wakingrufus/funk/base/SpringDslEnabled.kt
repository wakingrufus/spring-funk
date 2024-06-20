package com.github.wakingrufus.funk.base

import com.github.wakingrufus.funk.core.SpringDsl
import org.springframework.context.support.GenericApplicationContext

/**
 * useful in initializers written to access the corresponding DSL class
 */
inline fun <reified T : SpringDsl> GenericApplicationContext.getDsl(): T? {
    return applicationListeners
        .filterIsInstance<SpringDslListener>()
        .map { it.dsl }
        .map { it.get(T::class.java) }
        .firstOrNull()
}