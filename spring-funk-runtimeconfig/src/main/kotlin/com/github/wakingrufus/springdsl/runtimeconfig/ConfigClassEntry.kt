package com.github.wakingrufus.springdsl.runtimeconfig

data class ConfigClassEntry<T: Any>(
    val clazz: Class<T>,
    val prefix: String?
)