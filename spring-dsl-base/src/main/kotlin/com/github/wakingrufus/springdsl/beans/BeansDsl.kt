package com.github.wakingrufus.springdsl.beans

import com.github.wakingrufus.springdsl.core.SpringDsl
import com.github.wakingrufus.springdsl.core.SpringDslContainer
import com.github.wakingrufus.springdsl.core.SpringDslMarker
import org.springframework.context.support.BeanDefinitionDsl

class BeansDsl : SpringDsl {
    internal var beansDsl: BeanDefinitionDsl? = null

    @SpringDslMarker
    fun beans(config: BeanDefinitionDsl.() -> Unit) {
        beansDsl = org.springframework.context.support.beans(config)
    }
}

@SpringDslMarker
internal fun SpringDslContainer.beans(config: BeanDefinitionDsl.() -> Unit) {
    register(BeansDsl().apply { beans(config) })
}
