package com.github.wakingrufus.funk.htmx.route

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.wakingrufus.funk.htmx.template.HtmxTemplate
import com.github.wakingrufus.funk.htmx.template.template
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.html.stream.appendHTML
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.getBean
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.servlet.function.RouterFunctionDsl
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.contentTypeOrNull

class ParamRoute<CONTROLLER : Any, REQ : Record, RESP : Any>(
    val routerFunction: RouterFunctionDsl.(String, (ServerRequest) -> ServerResponse) -> Unit,
    val path: String,
    private val requestClass: Class<REQ>,
    private val controllerClass: Class<CONTROLLER>,
    val binding: CONTROLLER.(REQ) -> RESP,
    val renderer: HtmxTemplate<RESP>
) : HxRoute {
    private val log = KotlinLogging.logger {}
    override fun registerRoutes(beanFactory: BeanFactory, dsl: RouterFunctionDsl) {
        dsl.apply {
            routerFunction(path) { request ->
                val contentType = request.headers().contentTypeOrNull()
                val req = if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
                    request.body(requestClass)
                } else {
                    beanFactory.getBean<ObjectMapper>().convertValue(
                        MultiValueMap.fromMultiValue(
                            request.params()
                                .plus(MultiValueMap.fromSingleValue(request.pathVariables()))
                        ).toSingleValueMap(),
                        requestClass
                    )
                }
                val resp = beanFactory.getBean(controllerClass).binding(req)

                val acceptedType = request.headers().accept()
                if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType) && acceptedType.any {
                        MediaType.APPLICATION_JSON.isCompatibleWith(it)
                    }) {
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(resp)
                } else {
                    ServerResponse.ok().contentType(MediaType.TEXT_HTML)
                        .body(buildString {
                            appendHTML(false).template(renderer, resp)
                        })
                }
            }
        }
    }
}
