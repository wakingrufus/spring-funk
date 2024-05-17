package com.github.wakingrufus.springdsl.example

import com.github.wakingrufus.springdsl.base.SpringDslApplication
import com.github.wakingrufus.springdsl.core.SpringDslContainer
import com.github.wakingrufus.springdsl.webmvc.webmvc
import org.springframework.boot.SpringApplication
import org.springframework.web.servlet.function.ServerResponse

class ExampleApplication :SpringDslApplication {
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