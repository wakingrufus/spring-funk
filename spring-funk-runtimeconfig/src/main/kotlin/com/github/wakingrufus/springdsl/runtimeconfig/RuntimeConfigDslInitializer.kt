package com.github.wakingrufus.springdsl.runtimeconfig

import com.github.wakingrufus.funk.base.getDsl
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.cloud.context.environment.EnvironmentChangeEvent
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ApplicationListener
import org.springframework.context.support.GenericApplicationContext

/**
 * Spring implementation of {@link RuntimeConfigDsl}
 */
class RuntimeConfigDslInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        applicationContext.getDsl<RuntimeConfigDsl>()?.apply {
            configClasses.forEach { configClassEntry ->
                registerRuntimeConfig(
                    applicationContext,
                    configClassEntry.clazz,
                    configClassEntry.prefix
                )
            }
            applicationContext.addApplicationListener(Initializer(applicationContext))
            applicationContext.addApplicationListener(Updater(applicationContext))
        }
    }


    private class Initializer(val applicationContext: GenericApplicationContext) :
        ApplicationListener<ApplicationReadyEvent> {
        private val log = KotlinLogging.logger {}
        override fun onApplicationEvent(event: ApplicationReadyEvent) {
            log.info { "initializing RuntimeConfigs" }
            val runtimeConfigs = applicationContext.getBeansOfType(SpringRuntimeConfig::class.java)
            runtimeConfigs.forEach { (_, obj) ->
                obj.update()
            }
        }
    }

    private class Updater(val applicationContext: GenericApplicationContext) :
        ApplicationListener<EnvironmentChangeEvent> {
        private val log = KotlinLogging.logger {}
        override fun onApplicationEvent(event: EnvironmentChangeEvent) {
            log.info { "propagating runtime config updates to RuntimeConfigs" }
            val runtimeConfigs = applicationContext.getBeansOfType(SpringRuntimeConfig::class.java)
            runtimeConfigs.forEach { (_, obj) ->
                if (event.keys.stream().anyMatch { it.startsWith(obj.prefix) }) {
                    obj.update()
                }
            }
        }
    }
}
