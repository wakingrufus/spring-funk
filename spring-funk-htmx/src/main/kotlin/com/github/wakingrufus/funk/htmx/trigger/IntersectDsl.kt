package com.github.wakingrufus.funk.htmx.trigger

import com.github.wakingrufus.funk.htmx.AttributeModifier

class IntersectDsl {
    private val events: MutableSet<AttributeModifier> = mutableSetOf()
    fun root(selector: String) {
        events.add(AttributeModifier("root", selector))
    }

    fun threshold(amt: Double) {
        events.add(AttributeModifier("threshold", amt.toString()))
    }

    operator fun invoke(): HxTriggerEvent.Intersect {
        return HxTriggerEvent.Intersect(events)
    }
}