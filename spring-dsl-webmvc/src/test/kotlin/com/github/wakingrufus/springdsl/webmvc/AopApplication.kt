package com.github.wakingrufus.springdsl.webmvc

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableWebMvc
open class AopApplication {
    @Bean
    open fun controller(): Controller {
        return Controller()
    }
}