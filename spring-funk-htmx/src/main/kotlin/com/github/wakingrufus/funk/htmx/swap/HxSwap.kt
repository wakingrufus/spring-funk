package com.github.wakingrufus.funk.htmx.swap

import com.github.wakingrufus.funk.htmx.AttributeModifier

data class HxSwap(val type: HxSwapType, val modifiers: List<AttributeModifier>) {
    operator fun invoke(): String {
        return if (modifiers.isEmpty()) type.stringValue else "${type.stringValue} ${
            modifiers.joinToString(" ") { it() }
        }"
    }
}
