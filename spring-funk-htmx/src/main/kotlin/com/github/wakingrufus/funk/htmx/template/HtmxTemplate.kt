package com.github.wakingrufus.funk.htmx.template

import kotlinx.html.TagConsumer

fun interface HtmxTemplate<I> {
    fun render(appendable: TagConsumer<Any?>, input: I)
}

fun <I> htmxTemplate(template: TagConsumer<*>.(I) -> Unit): HtmxTemplate<I> {
    return HtmxTemplate { appendable, data -> template.invoke(appendable, data) }
}

fun <I> TagConsumer<*>.template(template: HtmxTemplate<I>, input: I) {
    template.render(this, input)
}
