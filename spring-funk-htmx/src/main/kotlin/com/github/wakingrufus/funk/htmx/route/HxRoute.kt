package com.github.wakingrufus.funk.htmx.route

import kotlinx.html.TagConsumer
import org.springframework.beans.factory.BeanFactory
import org.springframework.web.servlet.function.RouterFunctionDsl
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.w3c.dom.Document

interface HxRoute {
    fun registerRoutes(beanFactory: BeanFactory, dsl: RouterFunctionDsl)
}

fun <CONTROLLER : Any, REQ : Record, RESP : Any> withParam(
    routerFunction: RouterFunctionDsl.(String, (ServerRequest) -> ServerResponse) -> Unit,
    path: String,
    requestClass: Class<REQ>,
    controllerClass: Class<CONTROLLER>,
    binding: CONTROLLER.(REQ) -> RESP,
    renderer: (RESP) -> TagConsumer<Document>.() -> Document
): HxRoute {
    return ParamRoute(routerFunction, path, requestClass, controllerClass, binding, renderer)
}

fun <CONTROLLER : Any, RESP : Any> noParam(
    routerFunction: RouterFunctionDsl.(String, (ServerRequest) -> ServerResponse) -> Unit,
    path: String,
    controllerClass: Class<CONTROLLER>,
    binding: CONTROLLER.() -> RESP,
    renderer: (RESP) -> TagConsumer<Document>.() -> Document
): HxRoute {
    return NoParamRoute(routerFunction, path, controllerClass, binding, renderer)
}