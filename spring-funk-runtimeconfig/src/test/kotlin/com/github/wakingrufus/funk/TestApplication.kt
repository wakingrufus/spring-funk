package com.github.wakingrufus.funk

import com.github.wakingrufus.funk.base.SpringFunkApplication
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
