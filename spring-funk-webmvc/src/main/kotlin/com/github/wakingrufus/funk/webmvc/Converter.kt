package com.github.wakingrufus.funk.webmvc

import org.springframework.context.support.GenericApplicationContext

interface Converter {
    fun getInitializer(): GenericApplicationContext.() -> Unit
}