package com.github.wakingrufus.funk.example

import com.github.wakingrufus.funk.base.SpringFunkApplication
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.logging.logging
import com.github.wakingrufus.funk.webmvc.webmvc
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.SpringApplication
import org.springframework.boot.logging.LogLevel
import org.springframework.web.servlet.function.ServerResponse

class ExampleApplication : SpringFunkApplication {
    private val log = KotlinLogging.logger {}
    override fun dsl(): SpringDslContainer.() -> Unit = {
        logging {
            root(LogLevel.INFO)
            level("com.github.wakingrufus", LogLevel.INFO)
        }
        webmvc {
            enableWebMvc {
                jetty()
            }
            routes {
                route {
                    GET("/dsl") {
                        log.debug { "handling hello world request" }
                        ServerResponse.ok().body("Hello World")
                    }
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ExampleApplication::class.java, *args)
}