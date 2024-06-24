package com.github.wakingrufus.funk

data class ConfigClassEntry<T: Any>(
    val clazz: Class<T>,
    val prefix: String?
)