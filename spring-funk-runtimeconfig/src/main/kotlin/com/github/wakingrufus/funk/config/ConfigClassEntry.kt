package com.github.wakingrufus.funk.config

data class ConfigClassEntry<T: Any>(
    val clazz: Class<T>,
    val prefix: String?
)