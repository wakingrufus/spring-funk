package com.github.wakingrufus.funk.example

import com.github.wakingrufus.funk.base.SpringFunkApplication
import com.github.wakingrufus.funk.beans.beans
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.htmx.HttpVerb
import com.github.wakingrufus.funk.htmx.htmx
import com.github.wakingrufus.funk.htmx.hxPost
import com.github.wakingrufus.funk.htmx.swap.HxSwapType
import com.github.wakingrufus.funk.logging.logging
import com.github.wakingrufus.funk.webmvc.webmvc
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.span
import kotlinx.html.textInput
import org.springframework.boot.SpringApplication
import org.springframework.boot.logging.LogLevel
import org.springframework.web.servlet.function.ServerResponse

const val helloWorldUrl = "/load"

class ExampleApplication : SpringFunkApplication {
    private val log = KotlinLogging.logger {}
    override fun dsl(): SpringDslContainer.() -> Unit = {
        logging {
            root(LogLevel.INFO)
            level("com.github.wakingrufus", LogLevel.INFO)
        }
        beans {
            bean<ExampleService>()
        }
        htmx {
            page("/index") {

                route(HttpVerb.POST, helloWorldUrl, ExampleService::sayHello) {
                    div {
                        span {
                            +it.message
                        }
                    }
                }

                initialLoad {
                    form {
                        hxPost(helloWorldUrl) {
                            swap(HxSwapType.OuterHtml) {
                            }
                        }
                        textInput(name = "name") {
                        }
                        button(classes = "btn primary") {
                            +"Submit"
                        }
                    }
                    button {
                        hxPost(helloWorldUrl) {
                            swap(HxSwapType.OuterHtml)
                        }
                        +"Click Me"
                    }
                }
            }
        }
        webmvc {
            enableWebMvc {
                jetty()
            }
            converters {
                jackson()
                form()
                string()
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