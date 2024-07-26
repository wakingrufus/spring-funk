package com.github.wakingrufus.funk.logging

import com.github.wakingrufus.funk.base.getDsl
import org.springframework.boot.logging.LoggingSystem
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext

class LoggingDslInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        val loggingSystem: LoggingSystem = LoggingSystem.get(LoggingDslInitializer::class.java.classLoader)
        applicationContext.getDsl<LoggingDsl>()?.levels?.forEach { (name, level) ->
            loggingSystem.setLogLevel(name, level)
        }
    }
}
