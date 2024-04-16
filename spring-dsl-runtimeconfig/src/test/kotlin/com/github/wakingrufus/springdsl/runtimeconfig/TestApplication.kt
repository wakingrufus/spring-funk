package com.github.wakingrufus.springdsl.runtimeconfig

import com.github.wakingrufus.springdsl.base.SpringDslApplication
import com.github.wakingrufus.springdsl.core.SpringDslContainer
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration

@SpringBootConfiguration
@EnableAutoConfiguration
open class TestApplication : SpringDslApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        runtimeConfig {
            registerConfigClass<ConfigProps>()
        }
    }
}
