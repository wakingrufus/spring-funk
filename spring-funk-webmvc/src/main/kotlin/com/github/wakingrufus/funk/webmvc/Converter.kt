package com.github.wakingrufus.funk.webmvc

import org.springframework.beans.factory.BeanRegistrarDsl

interface Converter {
    fun register(): BeanRegistrarDsl.() -> Unit
}