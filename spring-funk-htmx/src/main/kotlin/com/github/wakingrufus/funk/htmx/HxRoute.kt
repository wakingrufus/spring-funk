package com.github.wakingrufus.funk.htmx

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.html.TagConsumer
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.serialize
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.getBean
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.RouterFunctionDsl
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.contentTypeOrNull
import org.w3c.dom.Document

class HxRoute<CONTROLLER : Any, REQ : Record, RESP : Record>(
    val routerFunction: RouterFunctionDsl.(String, (ServerRequest) -> ServerResponse) -> Unit,
    val path: String,
    private val requestClass: Class<REQ>,
    private val controllerClass: Class<CONTROLLER>,
    val binding: CONTROLLER.(REQ) -> RESP,
    val renderer: (RESP) -> TagConsumer<Document>.() -> Document
) {
    val log = KotlinLogging.logger {}

    fun registerRoutes(beanFactory: BeanFactory, dsl: RouterFunctionDsl) {
        dsl.apply {
            routerFunction(path) { request ->
                val contentType = request.headers().contentTypeOrNull()
                val req = if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
                    request.body(requestClass)
                } else if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(contentType)) {
                    beanFactory.getBean<ObjectMapper>()
                        .convertValue(request.params().toSingleValueMap(), requestClass)
                } else {
                    log.warn { "unsupported content type $contentType" }
                    null
                }
                val resp = beanFactory.getBean(controllerClass).binding(req!!)

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
