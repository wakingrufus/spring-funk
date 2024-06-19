package com.github.wakingrufus.springdsl.webmvc

import com.github.wakingrufus.springdsl.base.SpringFunkApplication
import com.github.wakingrufus.springdsl.core.SpringDslContainer
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.function.ServerResponse

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableWebMvc
open class AopApplication : SpringFunkApplication {
    @Bean
    open fun controller(): Controller {
        return Controller()
    }

    override fun dsl(): SpringDslContainer.() -> Unit = {
        webmvc {
            router {
                GET("dsl"){
                    ServerResponse.ok().body("dsl")
                }
            }
        }
    }
}