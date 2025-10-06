package com.github.wakingrufus.funk.htmx

import com.github.wakingrufus.funk.core.SpringDsl
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.core.SpringDslMarker
import com.github.wakingrufus.khtmx.spring.SpringHtmxDsl

@SpringDslMarker
class HtmxDsl(val dsl: SpringHtmxDsl) : SpringDsl

@SpringDslMarker
fun SpringDslContainer.htmx(config: SpringHtmxDsl.() -> Unit) {
    register(HtmxDsl(SpringHtmxDsl(config)))
}
