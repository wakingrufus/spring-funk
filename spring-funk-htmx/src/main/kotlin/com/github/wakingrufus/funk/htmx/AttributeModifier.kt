package com.github.wakingrufus.funk.htmx

class AttributeModifier (val name: String, val value: String? = null) {
    operator fun invoke(): String {
        return if (value == null) {
            name
        } else {
            "$name:$value"
        }
    }
}