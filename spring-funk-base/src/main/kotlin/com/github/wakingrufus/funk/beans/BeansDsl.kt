package com.github.wakingrufus.funk.beans

import com.github.wakingrufus.funk.core.SpringDsl
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.core.SpringDslMarker
import org.springframework.beans.factory.BeanRegistrarDsl
import org.springframework.context.support.BeanDefinitionDsl

@SpringDslMarker
class BeansDsl : SpringDsl {
    internal var beansDsl: BeanRegistrarDsl? = null

    @SpringDslMarker
    fun beans(config: BeanRegistrarDsl.() -> Unit) {
        beansDsl = BeanRegistrarDsl(config)
    }
}

@SpringDslMarker
fun SpringDslContainer.beans(config: BeanRegistrarDsl.() -> Unit) {
    register(BeansDsl().apply { beans(config) })
}
