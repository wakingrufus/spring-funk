package com.github.wakingrufus.funk

import com.github.wakingrufus.funk.base.SpringFunkApplication
import com.github.wakingrufus.funk.base.testDsl
import com.github.wakingrufus.funk.beans.beans
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
