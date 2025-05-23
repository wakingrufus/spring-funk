package com.github.wakingrufus.funk.htmx.route

import com.github.wakingrufus.funk.htmx.template.HtmxTemplate
import com.github.wakingrufus.funk.htmx.template.template
import kotlinx.html.stream.appendHTML
import org.springframework.beans.factory.BeanFactory
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.RouterFunctionDsl
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.contentTypeOrNull
import org.springframework.web.servlet.function.principalOrNull
import java.security.Principal

class AuthRoute<CONTROLLER : Any, USER : Principal, RESP : Any>(
    val routerFunction: RouterFunctionDsl.(String, (ServerRequest) -> ServerResponse) -> Unit,
    val path: String,
    private val controllerClass: Class<CONTROLLER>,
    val binding: CONTROLLER.(USER?) -> RESP,
    val renderer: HtmxTemplate<RESP>
) : HxRoute {
    override fun registerRoutes(beanFactory: BeanFactory, dsl: RouterFunctionDsl) {
        dsl.apply {
            routerFunction(path) { request ->
                val contentType = request.headers().contentTypeOrNull()
                val resp = beanFactory.getBean(controllerClass).binding(request.principalOrNull() as USER?)
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
