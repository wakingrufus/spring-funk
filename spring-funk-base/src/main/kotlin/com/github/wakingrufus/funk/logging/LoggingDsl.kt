package com.github.wakingrufus.funk.logging

import com.github.wakingrufus.funk.core.SpringDsl
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.core.SpringDslMarker
import org.springframework.boot.logging.LogLevel

@SpringDslMarker
class LoggingDsl : SpringDsl {
    internal val levels: MutableMap<String, LogLevel> = mutableMapOf()

    @SpringDslMarker
    fun root(level: LogLevel) {
        levels["ROOT"] = level
    }

    @SpringDslMarker
    fun level(packageName: String, level: LogLevel) {
        levels[packageName] = level
    }

    @SpringDslMarker
    inline fun <reified T> level(level: LogLevel) {
        T::class.qualifiedName?.also { level(it, level) }
    }
}

@SpringDslMarker
fun SpringDslContainer.logging(config: LoggingDsl.() -> Unit) {
    register(LoggingDsl().apply(config))
}
