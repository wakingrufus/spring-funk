package com.github.wakingrufus.springdsl.runtimeconfig

import com.github.wakingrufus.springdsl.base.SpringFunkApplication
import com.github.wakingrufus.funk.core.SpringDslContainer
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration

@SpringBootConfiguration
@EnableAutoConfiguration
open class TestApplication : SpringFunkApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        runtimeConfig {
            registerConfigClass<ConfigProps>()
        }
    }
}
