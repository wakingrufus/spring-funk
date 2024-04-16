package com.github.wakingrufus.springdsl.runtimeconfig

import com.github.wakingrufus.springdsl.base.getDsl
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.support.RootBeanDefinition
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.cloud.context.environment.EnvironmentChangeEvent
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ApplicationListener
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.ResolvableType
import java.util.function.Supplier

/**
 * Spring implementation of {@link RuntimeConfigDsl}
 */
class RuntimeConfigDslInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    private val log = KotlinLogging.logger {}
    override fun initialize(applicationContext: GenericApplicationContext) {
        applicationContext.getDsl<RuntimeConfigDsl>()?.apply {
            configClasses.forEach { configClassEntry ->
                log.info { configClassEntry.toString() }
                val beanDefinition = RootBeanDefinition(RuntimeConfig::class.java)
                beanDefinition.setTargetType(ResolvableType.forClassWithGenerics(RuntimeConfig::class.java, configClassEntry.clazz))
                beanDefinition.instanceSupplier = Supplier {
                    SpringRuntimeConfig.create(
                        configClassEntry.clazz,
                        applicationContext.environment,
                        configClassEntry.prefix
                    )
                }
                applicationContext.registerBeanDefinition(configClassEntry.clazz.name, beanDefinition)
            }
            applicationContext.addApplicationListener((ApplicationListener<ApplicationReadyEvent> {
                log.info { "initializing RuntimeConfigs" }
                val runtimeConfigs = applicationContext.getBeansOfType(SpringRuntimeConfig::class.java)
                runtimeConfigs.forEach { (_, u) -> u.update() }
            }))
            applicationContext.addApplicationListener((ApplicationListener<EnvironmentChangeEvent> { event ->
                log.info { "propagating runtime config updates to RuntimeConfigs" }
                val runtimeConfigs = applicationContext.getBeansOfType(SpringRuntimeConfig::class.java)
                runtimeConfigs.forEach { (_, obj) ->
                    if (event.keys.any { it.startsWith(obj.prefix) }) {
                        obj.update()
                    }
                }
            }))
        }
    }
}
