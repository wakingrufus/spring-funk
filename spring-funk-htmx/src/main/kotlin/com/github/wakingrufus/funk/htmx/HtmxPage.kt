package com.github.wakingrufus.funk.htmx

import com.github.wakingrufus.funk.core.SpringDslMarker
import com.github.wakingrufus.funk.htmx.route.HxRoute
import com.github.wakingrufus.funk.htmx.route.noParam
import com.github.wakingrufus.funk.htmx.route.withAuth
import com.github.wakingrufus.funk.htmx.route.withParam
import com.github.wakingrufus.funk.htmx.route.withParamAndAuth
import com.github.wakingrufus.funk.htmx.template.HtmxTemplate
import kotlinx.html.BODY
import kotlinx.html.TagConsumer
import kotlinx.html.body
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.serialize
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.script
import org.springframework.beans.factory.BeanFactory
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.RouterFunctionDsl
import org.springframework.web.servlet.function.ServerResponse
import java.security.Principal

class HtmxPage(val path: String) {
    var initialLoad: BODY.() -> Unit = {}
    internal var routes: MutableList<HxRoute> = mutableListOf()

    @SpringDslMarker
    fun initialLoad(dsl: BODY.() -> Unit) {
        initialLoad = dsl
    }

    operator fun invoke() {

    }

    fun addRoute(route: HxRoute) {
        routes.add(route)
    }

    /**
     * use when declaring a GET route that takes no parameters
     */
    @SpringDslMarker
    inline fun <reified CONTROLLER : Any, RESP : Any> get(
        path: String,
        noinline binding: CONTROLLER.() -> RESP,
        renderer: HtmxTemplate<RESP>
    ): HxRoute {
        return noParam(
            RouterFunctionDsl::GET,
            path = path,
            controllerClass = CONTROLLER::class.java,
            binding = binding,
            renderer = renderer
        )
            .also { addRoute(it) }
    }

    @SpringDslMarker
    inline fun <reified CONTROLLER : Any, RESP : Any> get(
        path: String,
        noinline binding: CONTROLLER.() -> RESP,
        crossinline renderer: TagConsumer<*>.(RESP) -> Unit
    ): HxRoute {
        return get(path, binding) { appendable, input -> renderer.invoke(appendable, input) }
    }

    /**
     * use when declaring a GET route that takes no parameters, but uses security
     */
    @SpringDslMarker
    inline fun <reified CONTROLLER : Any, USER : Principal, RESP : Any> get(
        path: String,
        noinline binding: CONTROLLER.(USER?) -> RESP,
        renderer: HtmxTemplate<RESP>
    ): HxRoute {
        return withAuth(
            RouterFunctionDsl::GET,
            path = path,
            controllerClass = CONTROLLER::class.java,
            binding = binding,
            renderer = renderer
        )
            .also { addRoute(it) }
    }

    @SpringDslMarker
    inline fun <reified CONTROLLER : Any, USER : Principal, RESP : Any> get(
        path: String,
        noinline binding: CONTROLLER.(USER?) -> RESP,
        crossinline renderer: TagConsumer<*>.(RESP) -> Unit
    ): HxRoute {
        return get(path, binding) { appendable, input -> renderer.invoke(appendable, input) }
    }

    @SpringDslMarker
    inline fun <reified CONTROLLER : Any, reified REQ : Record, RESP : Any> route(
        verb: HttpVerb,
        path: String,
        noinline binding: CONTROLLER.(REQ) -> RESP,
        crossinline renderer: TagConsumer<*>.(RESP) -> Unit
    ): HxRoute {
        return route(verb, path, binding) { appendable, input -> renderer.invoke(appendable, input) }
    }

    @SpringDslMarker
    inline fun <reified CONTROLLER : Any, reified REQ : Record, RESP : Any> route(
        verb: HttpVerb,
        path: String,
        noinline binding: CONTROLLER.(REQ) -> RESP,
        renderer: HtmxTemplate<RESP>
    ): HxRoute {
        return withParam(
            routerFunction = when (verb) {
                HttpVerb.GET -> RouterFunctionDsl::GET
                HttpVerb.POST -> RouterFunctionDsl::POST
                HttpVerb.PUT -> RouterFunctionDsl::PUT
                HttpVerb.PATCH -> RouterFunctionDsl::PATCH
                HttpVerb.DELETE -> RouterFunctionDsl::DELETE
            },
            path = path,
            requestClass = REQ::class.java,
            controllerClass = CONTROLLER::class.java,
            binding = binding,
            renderer = renderer
        )
            .also { addRoute(it) }
    }

    @SpringDslMarker
    inline fun <reified CONTROLLER : Any, reified REQ : Record, USER : Principal, RESP : Any> route(
        verb: HttpVerb,
        path: String,
        noinline binding: CONTROLLER.(USER?, REQ) -> RESP,
        crossinline renderer: TagConsumer<*>.(RESP) -> Unit
    ): HxRoute {
        return route(verb, path, binding) { appendable, input -> renderer.invoke(appendable, input) }
    }

    @SpringDslMarker
    inline fun <reified CONTROLLER : Any, reified REQ : Record, USER : Principal, RESP : Any> route(
        verb: HttpVerb,
        path: String,
        noinline binding: CONTROLLER.(USER?, REQ) -> RESP,
        renderer: HtmxTemplate<RESP>
    ): HxRoute {
        return withParamAndAuth(
            routerFunction = when (verb) {
                HttpVerb.GET -> RouterFunctionDsl::GET
                HttpVerb.POST -> RouterFunctionDsl::POST
                HttpVerb.PUT -> RouterFunctionDsl::PUT
                HttpVerb.PATCH -> RouterFunctionDsl::PATCH
                HttpVerb.DELETE -> RouterFunctionDsl::DELETE
            },
            path = path,
            requestClass = REQ::class.java,
            controllerClass = CONTROLLER::class.java,
            binding = binding,
            renderer = renderer
        )
            .also { addRoute(it) }
    }

    fun registerRoutes(beanFactory: BeanFactory, dsl: RouterFunctionDsl) {
        dsl.apply {
            GET(path) { request ->
                ServerResponse.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(
                        createHTMLDocument().html {
                            head {
                                script {
                                    src = "https://unpkg.com/htmx.org@2.0.4"
                                }
                            }
                            body {
                                initialLoad(this)
                            }
                        }.serialize(false)
                    )
            }
            routes.forEach { route ->
                route.registerRoutes(beanFactory, this)
            }
        }
    }
}