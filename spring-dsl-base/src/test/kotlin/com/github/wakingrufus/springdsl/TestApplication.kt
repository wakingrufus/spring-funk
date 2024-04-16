package com.github.wakingrufus.springdsl

import com.github.wakingrufus.springdsl.base.SpringDslApplication
import com.github.wakingrufus.springdsl.base.testDsl
import com.github.wakingrufus.springdsl.beans.beans
import com.github.wakingrufus.springdsl.core.SpringDslContainer
import org.springframework.boot.SpringBootConfiguration

@SpringBootConfiguration
open class TestApplication : SpringDslApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        testDsl {
            stringBean("stringbean", "1")
        }
        beans {
            bean<String>("bean") { "2" }
        }
    }
}
