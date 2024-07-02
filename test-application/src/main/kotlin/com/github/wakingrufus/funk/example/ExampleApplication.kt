package com.github.wakingrufus.funk.example

import com.github.wakingrufus.funk.base.SpringFunkApplication
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.webmvc.webmvc
import org.springframework.boot.SpringApplication
import org.springframework.web.servlet.function.ServerResponse

class ExampleApplication : SpringFunkApplication {
    override fun dsl(): SpringDslContainer.() -> Unit = {
        webmvc {
            enableWebMvc {
                jetty()
            }
            router {
                GET("/dsl") {
                    ServerResponse.ok().body("Hello World")
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ExampleApplication::class.java, *args)
}