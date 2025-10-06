package com.github.wakingrufus.funk.webmvc

import com.github.wakingrufus.funk.base.getDsl
import org.springframework.beans.factory.BeanRegistrarDsl
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext

class WebmvcInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) {
        context.getDsl<WebmvcDsl>()?.run {
            converterDsl?.run {
                context.register(this)
            }
            routers.forEach {
                context.register(BeanRegistrarDsl({
                    registerBean { it() }
                }))
            }
        }
    }
}