package com.github.wakingrufus.funk.htmx.trigger

import com.github.wakingrufus.funk.htmx.AttributeModifier
import com.github.wakingrufus.funk.htmx.toHtmx
import java.time.Duration

sealed class HxTriggerEvent(open val name: String) {
    abstract operator fun invoke(): String
    class Poll(val interval: Duration) : HxTriggerEvent("every") {
        override fun invoke(): String = "every " + interval.toHtmx()
    }

    class StandardEvent(override val name: String, val modifiers: List<AttributeModifier> = emptyList()) :
        HxTriggerEvent(name) {
        override fun invoke(): String = "$name " + modifiers.joinToString(" ") { it() }
    }

    object Load : HxTriggerEvent("load") {
        override fun invoke(): String = name
    }

    object Revealed : HxTriggerEvent("revealed") {
        override fun invoke(): String = name
    }

    class Intersect(val modifiers: Set<AttributeModifier> = emptySet()) : HxTriggerEvent("intersect") {
        override fun invoke(): String = "$name " + modifiers.joinToString(" ") { it() }
    }
}
