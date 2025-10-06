package com.github.wakingrufus.funk.webmvc

import com.github.wakingrufus.funk.core.SpringDsl
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.core.SpringDslMarker
import org.springframework.beans.factory.BeanRegistrarDsl
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctionDsl
import org.springframework.web.servlet.function.ServerResponse

@SpringDslMarker
class WebmvcDsl : SpringDsl {
    internal var routers: MutableList<BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.() -> RouterFunction<ServerResponse>> =
        mutableListOf()
    internal var converterDsl: WebMvcConverterDsl? = null

    fun addRouter(router: BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.() -> RouterFunction<ServerResponse>) {
        routers.add(router)
    }

    /**
     * top-level function for creating routes with single parameter bean injection
     */
    @SpringDslMarker
    inline fun router(crossinline f: RouterFunctionDsl.() -> Unit) {
        addRouter({ org.springframework.web.servlet.function.router { f(this) } })
    }

    /**
     * top-level function for creating routes with single parameter bean injection
     */
    @SpringDslMarker
    inline fun <reified P1 : Any> router(
        p1Name: String? = null,
        crossinline f: (dep1: P1) -> RouterFunctionDsl.() -> Unit
    ) {
        addRouter({ org.springframework.web.servlet.function.router { f(bean(p1Name))(this) } })
    }

    /**
     * top-level function for creating routes with 2 parameter bean injection
     */
    @SpringDslMarker
    inline fun <reified P1 : Any, reified P2 : Any> router(
        p1Name: String? = null,
        p2Name: String? = null,
        crossinline f: (dep1: P1, dep2: P2) -> RouterFunctionDsl.() -> Unit
    ) {
        addRouter { org.springframework.web.servlet.function.router { f(bean(p1Name), bean(p2Name))(this) } }
    }

    /**
     * top-level function for creating routes with 3 parameter bean injection
     */
    @SpringDslMarker
    inline fun <reified P1 : Any, reified P2 : Any, reified P3 : Any> router(
        p1Name: String? = null,
        p2Name: String? = null,
        p3Name: String? = null,
        crossinline f: (dep1: P1, dep2: P2, dep3: P3) -> RouterFunctionDsl.() -> Unit
    ) {
        addRouter {
            org.springframework.web.servlet.function.router {
                f(bean(p1Name), bean(p2Name), bean(p3Name))(
                    this
                )
            }
        }
    }

    /**
     * Configure converters via a [dedicated DSL][WebMvcConverterDsl].
     */
    @SpringDslMarker
    fun converters(init: WebMvcConverterDsl.() -> Unit = {}) {
        converterDsl = WebMvcConverterDsl(init)
    }
}

@SpringDslMarker
fun SpringDslContainer.webmvc(config: WebmvcDsl.() -> Unit) {
    register(WebmvcDsl().apply(config))
}
