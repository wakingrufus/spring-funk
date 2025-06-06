package com.github.wakingrufus.funk.htmx

import com.github.wakingrufus.funk.core.SpringDslMarker
import com.github.wakingrufus.funk.htmx.route.HxRoute
import kotlinx.html.BODY

class HtmxPage(val path: String) {
    var initialLoad: BODY.() -> Unit = {}
    internal var routes: MutableList<HxRoute> = mutableListOf()

    @SpringDslMarker
    fun initialLoad(dsl: BODY.() -> Unit) {
        initialLoad = dsl
    }
}