package com.github.wakingrufus.funk.htmx.trigger

class HxTrigger(val events: Set<HxTriggerEvent>) {
    operator fun invoke(): String {
        return events.joinToString(",") { it() }
    }
}
