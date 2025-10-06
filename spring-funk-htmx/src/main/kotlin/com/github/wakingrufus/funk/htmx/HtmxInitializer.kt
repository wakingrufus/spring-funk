package com.github.wakingrufus.funk.htmx

import com.github.wakingrufus.funk.base.getDsl
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean
import tools.jackson.databind.ObjectMapper

class HtmxInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        applicationContext.registerBean<ObjectMapper> { ObjectMapper() }
        applicationContext.getDsl<HtmxDsl>()?.run {
            applicationContext.register(dsl)
        }
    }
}