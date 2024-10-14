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

    /**
     * convenience method for when your route function only needs 1 injected parameter
     */
    @SpringDslMarker
    inline fun <reified P1> router(
        crossinline f: BeanDefinitionDsl.BeanSupplierContext.(dep1: P1) -> RouterFunctionDsl.() -> Unit
    ) {
        router { org.springframework.web.servlet.function.router { f(ref()).invoke(this) } }
    }

    /**
     * convenience method for when your route function only needs 2 injected parameters
     */
    @SpringDslMarker
    inline fun <reified P1, reified P2> router(
        crossinline f: BeanDefinitionDsl.BeanSupplierContext.(dep1: P1, dep2: P2) -> RouterFunctionDsl.() -> Unit
    ) {
        router { org.springframework.web.servlet.function.router { f(ref(), ref()).invoke(this) } }
    }

    /**
     * convenience method for when your route function only needs 3 injected parameters
     */
    @SpringDslMarker
    inline fun <reified P1, reified P2, reified P3> router(
        crossinline f: BeanDefinitionDsl.BeanSupplierContext.(dep1: P1, dep2: P2, dep3: P3) -> RouterFunctionDsl.() -> Unit
    ) {
        router { org.springframework.web.servlet.function.router { f(ref(), ref(), ref()).invoke(this) } }
    }

    @SpringDslMarker
    fun route(router: RouterFunctionDsl.() -> Unit) {
        routes.add { org.springframework.web.servlet.function.router(router) }
    }

    internal fun merge(f: BeanDefinitionDsl.BeanSupplierContext): RouterFunction<ServerResponse> =
        routes.map { it.invoke(f) }.reduce(RouterFunction<ServerResponse>::and)
}