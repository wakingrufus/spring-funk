package com.github.wakingrufus.funk.beans

import com.github.wakingrufus.funk.base.getDsl
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext

class BeanDslInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        applicationContext.getDsl<BeansDsl>()?.beansDsl?.initialize(applicationContext)
    }
}