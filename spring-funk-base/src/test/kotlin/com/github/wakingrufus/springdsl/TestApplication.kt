package com.github.wakingrufus.springdsl

import com.github.wakingrufus.springdsl.base.SpringFunkApplication
import com.github.wakingrufus.springdsl.base.testDsl
import com.github.wakingrufus.springdsl.beans.beans
import com.github.wakingrufus.funk.core.SpringDslContainer

class TestApplication : SpringFunkApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        testDsl {
            stringBean("stringbean", "1")
        }
        beans {
            bean<String>("bean") { "2" }
        }
    }
}
