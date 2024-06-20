package com.github.wakingrufus.springdsl.runtimeconfig

import com.github.wakingrufus.funk.util.normalizeConfigKey
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.getBeansOfType
import org.springframework.beans.factory.support.RootBeanDefinition
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.ResolvableType

/**
 * Use from a Java initializer to register a [RuntimeConfig].
 * Applications should not use this, and should use [RuntimeConfigDsl] instead.
 * @return bean name
 */
fun <T : Any> registerRuntimeConfig(
    applicationContext: GenericApplicationContext,
    configClass: Class<T>,
    prefix: String? = null,
    defaultInstance: T? = null
): String {
    val resolvableType = ResolvableType.forClassWithGenerics(RuntimeConfig::class.java, configClass)
    val beanDef = RootBeanDefinition(resolveBeanType()) {
        SpringRuntimeConfig.bind(
            configClass,
            applicationContext.environment,
            prefix?.let { normalizeConfigKey(it) },
            defaultInstance
        )
    }
    beanDef.setTargetType(resolvableType)
    val beanName = prefix?.let { configClass.simpleName + "-" + normalizeConfigKey(prefix) } ?: configClass.simpleName
    applicationContext.registerBeanDefinition(beanName, beanDef)
    return beanName

}

/**
 * Use from a Kotlin initializer to register a [RuntimeConfig].
 * Applications should not use this, and should use [RuntimeConfigDsl] instead.
 */
inline fun <reified T : Any> GenericApplicationContext.registerRuntimeConfig(
    prefix: String? = null,
    defaultInstance: T? = null
) {
    registerRuntimeConfig(this, T::class.java, prefix, defaultInstance)
}

private fun <T : Any> resolveBeanType(): Class<SpringRuntimeConfig<T>> {
    return resolveType<SpringRuntimeConfig<T>>()
}

internal inline fun <reified T> resolveType(): Class<T> {
    return T::class.java
}

/**
 * Use from a java initializer to access a [RuntimeConfig].
 * Applications should not use this, and should inject in the instance instead.
 */
fun <T : Any> getRuntimeConfig(beanFactory: ListableBeanFactory, configClass: Class<T>, prefix: String? = null): T? {
    val beans = beanFactory.getBeansOfType<RuntimeConfig<T>>()
    return if (prefix == null) {
        beans.values.firstOrNull()?.get()
    } else {
        val fullPrefix = configClass.simpleName + "-" + normalizeConfigKey(prefix)
        beanFactory.getBeansOfType<SpringRuntimeConfig<T>>()
            .get(fullPrefix)
            ?.get()
    }
}

/**
 * Use from a kotlin initializer to access a [RuntimeConfig].
 * Applications should not use this, and should inject in the instance instead.
 */
inline fun <reified T : Any> ListableBeanFactory.getRuntimeConfig(prefix: String? = null): T? {
    return getRuntimeConfig(this, T::class.java, prefix)
}
