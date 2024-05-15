package com.github.wakingrufus.springdsl.webmvc

import com.github.wakingrufus.springdsl.base.SpringDslApplication
import com.github.wakingrufus.springdsl.core.SpringDslContainer
import org.springframework.boot.SpringBootConfiguration
import org.springframework.web.servlet.function.ServerResponse

@SpringBootConfiguration
open class DslApplication : SpringDslApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        webmvc {
            enableWebMvc {
                jetty()
            }
            router {
                GET("/dsl") {
                    ServerResponse.ok().build()
                }
            }
        }
    }
}
