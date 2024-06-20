package com.github.wakingrufus.funk.beans

import com.github.wakingrufus.funk.core.SpringDsl
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.core.SpringDslMarker
import org.springframework.context.support.BeanDefinitionDsl

class BeansDsl : SpringDsl {
    internal var beansDsl: BeanDefinitionDsl? = null

    @SpringDslMarker
    fun beans(config: BeanDefinitionDsl.() -> Unit) {
        beansDsl = org.springframework.context.support.beans(config)
    }
}

@SpringDslMarker
fun SpringDslContainer.beans(config: BeanDefinitionDsl.() -> Unit) {
    register(BeansDsl().apply { beans(config) })
}
