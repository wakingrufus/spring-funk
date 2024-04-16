package com.github.wakingrufus.springdsl.base

import com.github.wakingrufus.springdsl.core.SpringDsl
import com.github.wakingrufus.springdsl.core.SpringDslContainer
import com.github.wakingrufus.springdsl.core.SpringDslMarker

/**
 * An example of a DSL written in Kotlin
 */
internal class TestDsl : SpringDsl {
    internal val stringBeans: MutableMap<String, String> = mutableMapOf()

    fun stringBean(name: String, value: String): TestDsl {
        stringBeans[name] = value
        return this
    }
}

@SpringDslMarker
internal fun SpringDslContainer.testDsl(config: TestDsl.() -> Unit) {
    configure(config)
}
