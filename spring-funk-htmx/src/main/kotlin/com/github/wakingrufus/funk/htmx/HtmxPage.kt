package com.github.wakingrufus.funk.htmx

import com.github.wakingrufus.funk.core.SpringDslMarker
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
import org.w3c.dom.Document

class HtmxPage(val path: String) {
    var initialLoad: BODY.() -> Unit = {}
    internal var routes: MutableList<HxRoute<*, *, *>> = mutableListOf()

    @SpringDslMarker
    fun initialLoad(dsl: BODY.() -> Unit) {
        initialLoad = dsl
    }

    operator fun invoke() {

    }

    fun addRoute(route: HxRoute<*, *, *>) {
        routes.add(route)
    }

    @SpringDslMarker
    inline fun <reified CONTROLLER : Any, reified REQ : Record, RESP : Record> route(
        verb: HttpVerb,
        path: String,
        noinline binding: CONTROLLER.(REQ) -> RESP,
        noinline renderer: (RESP) -> TagConsumer<Document>.() -> Document
    ): HxRoute<CONTROLLER, REQ, RESP> {
        return HxRoute(
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