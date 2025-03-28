package com.github.wakingrufus.funk.htmx

import com.github.wakingrufus.funk.core.SpringDsl
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.core.SpringDslMarker

@SpringDslMarker
class HtmxDsl : SpringDsl {
    val pages = ArrayList<HtmxPage>()

    @SpringDslMarker
    fun page(path: String, config: HtmxPage.() -> Unit) {
        pages.add(HtmxPage(path).apply(config))
    }
}

@SpringDslMarker
fun SpringDslContainer.htmx(config: HtmxDsl.() -> Unit) {
    register(HtmxDsl().apply(config))
}
