package com.github.wakingrufus.funk.htmx

import com.github.wakingrufus.funk.core.SpringDslMarker
import com.github.wakingrufus.funk.htmx.swap.HxSwap
import com.github.wakingrufus.funk.htmx.swap.HxSwapType
import kotlinx.html.HTMLTag

@SpringDslMarker
fun HTMLTag.hxTrigger(verb: HttpVerb, path: String, dsl: TriggerDsl.() -> Unit = {}) {
    TriggerDsl(verb, path).apply(dsl).invoke(this)
}

@SpringDslMarker
fun HTMLTag.hxSelect(selector: String) {
    attributes["hx-select"] = selector
}

@SpringDslMarker
fun HTMLTag.hxParams(params: String) {
    attributes["hx-params"] = params
}

@SpringDslMarker
fun HTMLTag.hxPushUrl(value: Boolean) {
    attributes["hx-push-url"] = value.toString()
}

@SpringDslMarker
fun HTMLTag.hxSwap(value: String) {
    attributes["hx-swap"] = value
}

@SpringDslMarker
fun HTMLTag.hxTarget(selector: String) {
    attributes["hx-target"] = selector
}

@SpringDslMarker
fun HTMLTag.hxVals(json: String) {
    attributes["hx-vals"] = json
}

@SpringDslMarker
fun HTMLTag.hxPost(path: String, dsl: TriggerDsl.() -> Unit = {}) {
    hxTrigger(HttpVerb.POST, path, dsl)
}

@SpringDslMarker
fun HTMLTag.hxGet(path: String, dsl: TriggerDsl.() -> Unit = {}) {
    hxTrigger(HttpVerb.GET, path, dsl)
}

@SpringDslMarker
fun HTMLTag.hxPut(path: String, dsl: TriggerDsl.() -> Unit = {}) {
    hxTrigger(HttpVerb.PUT, path, dsl)
}

@SpringDslMarker
fun HTMLTag.hxPatch(path: String, dsl: TriggerDsl.() -> Unit = {}) {
    hxTrigger(HttpVerb.PATCH, path, dsl)
}

@SpringDslMarker
fun HTMLTag.hxDelete(path: String, dsl: TriggerDsl.() -> Unit = {}) {
    hxTrigger(HttpVerb.DELETE, path, dsl)
}

@SpringDslMarker
fun HTMLTag.htmxIndicator() {
    attributes["class"] = "htmx-indicator"
}
