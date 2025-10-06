package com.github.wakingrufus.funk.example

import com.github.wakingrufus.funk.base.SpringFunkApplication
import com.github.wakingrufus.funk.beans.beans
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.htmx.htmx
import com.github.wakingrufus.funk.logging.logging
import com.github.wakingrufus.funk.webmvc.webmvc
import com.github.wakingrufus.htmx.HttpVerb
import com.github.wakingrufus.htmx.hxPost
import com.github.wakingrufus.htmx.swap.HxSwapType
import com.github.wakingrufus.htmx.template.htmxTemplate
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

val responseTemplate = htmxTemplate<HelloWorldResponse> {
    div {
        span {
            +it.message
        }
    }
}

class ExampleApplication : SpringFunkApplication {
    private val log = KotlinLogging.logger {}
    override fun dsl(): SpringDslContainer.() -> Unit = {
        logging {
            root(LogLevel.INFO)
            level("com.github.wakingrufus", LogLevel.INFO)
        }
        beans {
            registerBean<ExampleService>()
        }
        htmx {
            page("/index") {
                form {
                    hxPost(helloWorldUrl) {
                        hxSwap {
                            style(HxSwapType.OuterHtml)
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
                        hxSwap {
                            style(HxSwapType.OuterHtml)
                        }
                    }
                    +"Click Me"
                }
            }
            route(HttpVerb.POST, helloWorldUrl, ExampleService::sayHello, responseTemplate)
            route(HttpVerb.GET, "thing/{id}", ExampleService::getThingById, responseTemplate)

        }
        webmvc {

            converters {
                jackson()
                form()
                string()
            }
            router {
                GET("/dsl") {
                    log.debug { "handling hello world request" }
                    ServerResponse.ok().body("Hello World")
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ExampleApplication::class.java, *args)
}