package com.github.wakingrufus.funk.example

class ExampleService {
    fun sayHello(request: HelloWorldRequest): HelloWorldResponse {
        return if (request.name != null) {
            HelloWorldResponse("Hello ${request.name}")
        } else {
            HelloWorldResponse("Hello World")
        }
    }
}
