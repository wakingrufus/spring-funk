package com.github.wakingrufus.funk.webmvc

import com.github.wakingrufus.funk.core.SpringDslMarker
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctionDsl
import org.springframework.web.servlet.function.ServerResponse

@SpringDslMarker
class RoutesDsl {
    private val routes = mutableListOf<BeanDefinitionDsl.BeanSupplierContext.() -> RouterFunction<ServerResponse>>()

    @SpringDslMarker
    fun router(f: BeanDefinitionDsl.BeanSupplierContext.() -> RouterFunction<ServerResponse>) {
        routes.add(f)
    }

    @SpringDslMarker
    fun route(router: RouterFunctionDsl.() -> Unit) {
        routes.add { org.springframework.web.servlet.function.router(router) }
    }

    internal fun merge(f: BeanDefinitionDsl.BeanSupplierContext): RouterFunction<ServerResponse> =
        routes.map { it.invoke(f) }.reduce(RouterFunction<ServerResponse>::and)
}