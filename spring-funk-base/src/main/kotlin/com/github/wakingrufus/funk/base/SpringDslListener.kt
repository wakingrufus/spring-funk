package com.github.wakingrufus.funk.base

import com.github.wakingrufus.funk.core.SpringDslContainer
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent
import org.springframework.context.ApplicationListener

class SpringDslListener(val dsl: SpringDslContainer) : ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    override fun onApplicationEvent(event: ApplicationEnvironmentPreparedEvent) {

    }
}