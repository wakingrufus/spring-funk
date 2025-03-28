package com.github.wakingrufus.funk.htmx.trigger

import com.github.wakingrufus.funk.core.SpringDslMarker
import com.github.wakingrufus.funk.htmx.AttributeModifier
import com.github.wakingrufus.funk.htmx.toHtmx
import java.time.Duration

@SpringDslMarker
class StandardEventDsl(val name: String) {
    private val modifiers: MutableList<AttributeModifier> = mutableListOf()

    @SpringDslMarker
    fun once() {
        modifiers.add(AttributeModifier("once"))
    }

    @SpringDslMarker
    fun changed() {
        modifiers.add(AttributeModifier("changed"))
    }

    @SpringDslMarker
    fun delay(duration: Duration) {
        modifiers.add(AttributeModifier("delay", duration.toHtmx()))
    }

    @SpringDslMarker
    fun throttle(duration: Duration) {
        modifiers.add(AttributeModifier("throttle", duration.toHtmx()))
    }

    @SpringDslMarker
    fun from(selector: String) {
        modifiers.add(AttributeModifier("from", selector))
    }

    @SpringDslMarker
    fun target(selector: String) {
        modifiers.add(AttributeModifier("target", selector))
    }

    @SpringDslMarker
    fun consume() {
        modifiers.add(AttributeModifier("consume"))
    }

    @SpringDslMarker
    fun queue(option: QueueOption) {
        modifiers.add(AttributeModifier("queue", option.stringValue))
    }

    operator fun invoke(): HxTriggerEvent {
        return HxTriggerEvent.StandardEvent(name, modifiers)
    }

    enum class QueueOption(val stringValue: String) {
        First("first"), Last("last"), All("all"), None("none")
    }
}