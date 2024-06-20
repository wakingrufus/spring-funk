package com.github.wakingrufus.funk.base

import com.github.wakingrufus.funk.core.SpringDsl
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.core.SpringDslMarker

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
