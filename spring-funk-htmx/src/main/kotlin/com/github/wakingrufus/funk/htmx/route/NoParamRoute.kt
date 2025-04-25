package com.github.wakingrufus.funk.htmx.route

import kotlinx.html.TagConsumer
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.serialize
import org.springframework.beans.factory.BeanFactory
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.RouterFunctionDsl
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.contentTypeOrNull
import org.w3c.dom.Document

class NoParamRoute<CONTROLLER : Any, RESP : Any>(
    val routerFunction: RouterFunctionDsl.(String, (ServerRequest) -> ServerResponse) -> Unit,
    val path: String,
    private val controllerClass: Class<CONTROLLER>,
    val binding: CONTROLLER.() -> RESP,
    val renderer: (RESP) -> TagConsumer<Document>.() -> Document
) : HxRoute {
    override fun registerRoutes(beanFactory: BeanFactory, dsl: RouterFunctionDsl) {
        dsl.apply {
            routerFunction(path) { request ->
                val contentType = request.headers().contentTypeOrNull()
                val resp = beanFactory.getBean(controllerClass).binding()

                val acceptedType = request.headers().accept()
                if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType) && acceptedType.any {
                        MediaType.APPLICATION_JSON.isCompatibleWith(it)
                    }) {
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(resp)
                } else {
                    ServerResponse.ok().contentType(MediaType.TEXT_HTML)
                        .body(createHTMLDocument().run { renderer.invoke(resp).invoke(this) }.serialize(false))
                }
            }
        }
    }
}