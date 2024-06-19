package com.github.wakingrufus.springdsl.webmvc

import org.springframework.context.support.GenericApplicationContext

interface Converter {
    fun getInitializer(): GenericApplicationContext.() -> Unit
}