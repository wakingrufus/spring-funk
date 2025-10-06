package com.github.wakingrufus.funk.webmvc

import com.github.wakingrufus.funk.core.SpringDslMarker
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctionDsl
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.beans.factory.BeanRegistrarDsl
import org.springframework.web.servlet.function.router

/**
 * top-level function for creating routes with single parameter bean injection
 */
@SpringDslMarker
inline fun <reified P1 : Any> router(
    p1Name: String? = null,
    crossinline f: (dep1: P1) -> RouterFunctionDsl.() -> Unit
): BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.() -> RouterFunction<ServerResponse> {
    return { router { f(bean(p1Name))(this) } }
}

/**
 * top-level function for creating routes with 2 parameter bean injection
 */
@SpringDslMarker
inline fun <reified P1 : Any, reified P2 : Any> router(
    p1Name: String? = null,
    p2Name: String? = null,
    crossinline f: (dep1: P1, dep2: P2) -> RouterFunctionDsl.() -> Unit
): BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.() -> RouterFunction<ServerResponse> {
    return { router { f(bean(p1Name), bean(p2Name))(this) } }
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
): BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.() -> RouterFunction<ServerResponse> {
    return { router { f(bean(p1Name), bean(p2Name), bean(p3Name))(this) } }
}

/**
 * convenience method for creating injected routes inline within the [BeanRegistrarDsl]
 */
@SpringDslMarker
inline fun <reified P1 : Any> BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.routes(
    p1Name: String? = null,
    crossinline f: BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.(dep1: P1) -> RouterFunctionDsl.() -> Unit
): RouterFunction<ServerResponse> {
    return router<P1>(p1Name, { p1 -> f(p1) })(this)
}

/**
 * convenience method for creating injected routes inline within the [BeanRegistrarDsl]
 */
@SpringDslMarker
inline fun <reified P1 : Any, reified P2 : Any> BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.routes(
    p1Name: String? = null,
    p2Name: String? = null,
    crossinline f: BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.(dep1: P1, dep2: P2) -> RouterFunctionDsl.() -> Unit
): RouterFunction<ServerResponse> {
    return router<P1, P2>(p1Name, p2Name, { p1, p2 -> f(p1, p2) })(this)
}

/**
 * convenience method for creating injected routes inline within the [BeanRegistrarDsl]
 */
@SpringDslMarker
inline fun <reified P1 : Any, reified P2 : Any, reified P3 : Any> BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.routes(
    p1Name: String? = null,
    p2Name: String? = null,
    p3Name: String? = null,
    crossinline f: BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.(dep1: P1, dep2: P2, dep3: P3) -> RouterFunctionDsl.() -> Unit
): RouterFunction<ServerResponse> {
    return router<P1, P2, P3>(p1Name, p2Name, p3Name, { p1, p2, p3 -> f(p1, p2, p3) })(this)
}
