package com.github.wakingrufus.funk.htmx.route

import com.github.wakingrufus.funk.htmx.template.HtmxTemplate
import org.springframework.beans.factory.BeanFactory
import org.springframework.web.servlet.function.RouterFunctionDsl
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.security.Principal

interface HxRoute {
    fun registerRoutes(beanFactory: BeanFactory, dsl: RouterFunctionDsl)
}

fun <CONTROLLER : Any, REQ : Record, RESP : Any> withParam(
    routerFunction: RouterFunctionDsl.(String, (ServerRequest) -> ServerResponse) -> Unit,
    path: String,
    requestClass: Class<REQ>,
    controllerClass: Class<CONTROLLER>,
    binding: CONTROLLER.(REQ) -> RESP,
    renderer: HtmxTemplate<RESP>
): HxRoute {
    return ParamRoute(routerFunction, path, requestClass, controllerClass, binding, renderer)
}

fun <CONTROLLER : Any, RESP : Any> noParam(
    routerFunction: RouterFunctionDsl.(String, (ServerRequest) -> ServerResponse) -> Unit,
    path: String,
    controllerClass: Class<CONTROLLER>,
    binding: CONTROLLER.() -> RESP,
    renderer: HtmxTemplate<RESP>
): HxRoute {
    return NoParamRoute(routerFunction, path, controllerClass, binding, renderer)
}


fun <CONTROLLER : Any, USER : Principal, RESP : Any> withAuth(
    routerFunction: RouterFunctionDsl.(String, (ServerRequest) -> ServerResponse) -> Unit,
    path: String,
    controllerClass: Class<CONTROLLER>,
    binding: CONTROLLER.(USER?) -> RESP,
    renderer: HtmxTemplate<RESP>
): HxRoute {
    return AuthRoute(routerFunction, path, controllerClass, binding, renderer)
}


fun <CONTROLLER : Any, REQ : Record, USER : Principal, RESP : Any> withParamAndAuth(
    routerFunction: RouterFunctionDsl.(String, (ServerRequest) -> ServerResponse) -> Unit,
    path: String,
    requestClass: Class<REQ>,
    controllerClass: Class<CONTROLLER>,
    binding: CONTROLLER.(USER?, REQ) -> RESP,
    renderer: HtmxTemplate<RESP>
): HxRoute {
    return ParamAuthRoute(routerFunction, path, requestClass, controllerClass, binding, renderer)
}