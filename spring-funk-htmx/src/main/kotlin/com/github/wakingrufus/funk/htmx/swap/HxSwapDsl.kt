package com.github.wakingrufus.funk.htmx.swap

import com.github.wakingrufus.funk.core.SpringDslMarker
import com.github.wakingrufus.funk.htmx.AttributeModifier
import com.github.wakingrufus.funk.htmx.toHtmx
import java.time.Duration

@SpringDslMarker
class HxSwapDsl(val type: HxSwapType) {
    private val modifiers = mutableListOf<AttributeModifier>()

    @SpringDslMarker
    fun transition() {
        modifiers.add(AttributeModifier("transition"))
    }

    @SpringDslMarker
    fun swap(duration: Duration) {
        modifiers.add(AttributeModifier("swap", duration.toHtmx()))
    }

    @SpringDslMarker
    fun settle(duration: Duration) {
        modifiers.add(AttributeModifier("settle", duration.toHtmx()))
    }

    @SpringDslMarker
    fun ignoreTitle(enabled: Boolean = true) {
        modifiers.add(AttributeModifier("ignoreTitle", enabled.toString()))
    }

    operator fun invoke(): HxSwap {
        return HxSwap(type, modifiers)
    }
}
