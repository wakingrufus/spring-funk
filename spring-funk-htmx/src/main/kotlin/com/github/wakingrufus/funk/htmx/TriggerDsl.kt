package com.github.wakingrufus.funk.htmx

import com.github.wakingrufus.funk.core.SpringDslMarker
import com.github.wakingrufus.funk.htmx.swap.HxSwap
import com.github.wakingrufus.funk.htmx.swap.HxSwapDsl
import com.github.wakingrufus.funk.htmx.swap.HxSwapType
import com.github.wakingrufus.funk.htmx.trigger.HxTrigger
import com.github.wakingrufus.funk.htmx.trigger.HxTriggerDsl
import kotlinx.html.HTMLTag

@SpringDslMarker
class TriggerDsl(val verb: HttpVerb, val path: String) {
    private var swap: HxSwap? = null
    private var trigger: HxTrigger? = null
    private var hxParams: String? = null

    @SpringDslMarker
    var target: String? = null

    @SpringDslMarker
    var hxPushUrl: Boolean? = null

    @SpringDslMarker
    var hxSelect: String? = null

    @SpringDslMarker
    fun swap(type: HxSwapType, dsl: HxSwapDsl.() -> Unit = {}) {
        swap = HxSwapDsl(type).apply(dsl)()
    }

    @SpringDslMarker
    fun trigger(dsl: HxTriggerDsl.() -> Unit = {}) {
        trigger = HxTriggerDsl().apply(dsl)()
    }

    @SpringDslMarker
    fun allParams() {
        hxParams = "*"
    }

    @SpringDslMarker
    fun noParams() {
        hxParams = "none"
    }

    @SpringDslMarker
    fun excludeParams(vararg params: String) {
        hxParams = "not " + params.joinToString(",")
    }

    @SpringDslMarker
    fun includeParams(vararg params: String) {
        hxParams = params.joinToString(",")
    }

    operator fun invoke(element: HTMLTag) {
        element.apply {
            attributes["hx-${verb.attrName}"] = path
            swap?.let { hxSwap(it()) }
            trigger?.let { attributes["hx-trigger"] = it() }
            target?.let { hxTarget(it) }
            hxPushUrl?.let { hxPushUrl(it) }
            hxSelect?.let { hxSelect(it) }
            hxParams?.let { hxParams(it) }
        }
    }
}
