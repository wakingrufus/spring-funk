package com.github.wakingrufus.springdsl.webmvc

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {
    @GetMapping("/controller")
    fun controllerEndpoint(): ResponseEntity<*> {
        return ResponseEntity.ok().body("")
    }
}