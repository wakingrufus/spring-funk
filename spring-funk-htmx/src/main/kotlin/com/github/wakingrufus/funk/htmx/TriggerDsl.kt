package com.github.wakingrufus.funk.htmx

import com.github.wakingrufus.funk.core.SpringDslMarker
import com.github.wakingrufus.funk.htmx.swap.HxSwap
import com.github.wakingrufus.funk.htmx.swap.HxSwapDsl
import com.github.wakingrufus.funk.htmx.swap.HxSwapType
import com.github.wakingrufus.funk.htmx.trigger.HxTrigger
import com.github.wakingrufus.funk.htmx.trigger.HxTriggerDsl
import kotlinx.html.HTMLTag

@SpringDslMarker
class TriggerDsl(
    val verb: HttpVerb,
    val path: String,
    var target: String? = null,
    var hxPushUrl: Boolean? = null,
    var hxSelect: String? = null
) {
    private var swap: HxSwap? = null
    private var trigger: HxTrigger? = null

    @SpringDslMarker
    fun swap(type: HxSwapType, dsl: HxSwapDsl.() -> Unit = {}) {
        swap = HxSwapDsl(type).apply(dsl)()
    }

    @SpringDslMarker
    fun trigger(dsl: HxTriggerDsl.() -> Unit = {}) {
        trigger = HxTriggerDsl().apply(dsl)()
    }


    operator fun invoke(element: HTMLTag) {
        element.apply {
            attributes["hx-${verb.attrName}"] = path
            swap?.let { hxSwap(it()) }
            trigger?.let { attributes["hx-trigger"] = it() }
            target?.let { hxTarget(it) }
            hxPushUrl?.let { hxPushUrl(it) }
            hxSelect?.let { hxSelect(it) }
        }
    }
}
