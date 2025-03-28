package com.github.wakingrufus.funk.htmx

import com.github.wakingrufus.funk.base.getDsl
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean
import org.springframework.web.servlet.function.router

class HtmxInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        applicationContext.getDsl<HtmxDsl>()?.pages?.forEach { page ->
            val routesForPage = router {
                page.registerRoutes(applicationContext, this)
            }
            applicationContext.registerBean(page.path.replace("/", "")) {
                routesForPage
            }
        }
    }
}