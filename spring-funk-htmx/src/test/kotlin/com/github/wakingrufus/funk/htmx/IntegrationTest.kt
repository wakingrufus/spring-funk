package com.github.wakingrufus.funk.htmx

import com.github.wakingrufus.funk.beans.BeanDslInitializer
import com.github.wakingrufus.funk.beans.beans
import com.github.wakingrufus.funk.test.testDslApplication
import com.github.wakingrufus.funk.webmvc.WebmvcInitializer
import com.github.wakingrufus.funk.webmvc.webmvc
import com.github.wakingrufus.htmx.HttpVerb
import kotlinx.html.div
import kotlinx.html.li
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.security.Principal

internal class IntegrationTest {

    @Test
    fun test() {
        testDslApplication(WebmvcInitializer(), BeanDslInitializer(), HtmxInitializer()) {
            environment {
                setProperty("server.port", "0")
            }
            mockWebApplication {
                beans {
                    registerBean<Controller>()
                }
                webmvc {
                    converters {
                        jackson()
                        form()
                        string()
                    }
                }
                htmx {
                    page("/index") {
                        div {
                        }
                    }
                    get("/list", Controller::getList) {
                        it.forEach {
                            li { +it }
                        }
                    }

                    route(HttpVerb.GET, "/hello/{name}", Controller::hello) {
                        div { +it }
                    }

                    route(HttpVerb.GET, "/helloauth/{name}", Controller::sayHelloWithAuth) {
                        div { +it }
                    }

                    get("/helloauth", Controller::sayHelloWithAuthOnly) {
                        div { +it }
                    }
                }
            }
            test {
                assertThat(mockMvc).isNotNull
                mockMvc!!.run {
                    val indexRequest = MockMvcRequestBuilders.get("/index")
                        .accept(MediaType.TEXT_HTML)
                    val indexResponse = perform(indexRequest).andReturn().response
                    assertThat(indexResponse).isNotNull()
                    assertThat(indexResponse.status).isEqualTo(200)

                    val getRequest = MockMvcRequestBuilders.get("/list")
                        .accept(MediaType.TEXT_HTML)
                    val getResponse = perform(getRequest).andReturn().response
                    assertThat(getResponse).isNotNull()
                    assertThat(getResponse.status).isEqualTo(200)

                    val helloRequest = MockMvcRequestBuilders.get("/hello/John")
                        .accept(MediaType.TEXT_HTML)
                    val helloResponse = perform(helloRequest).andReturn().response
                    assertThat(helloResponse).isNotNull()
                    assertThat(helloResponse.status).isEqualTo(200)
                    assertThat(helloResponse.contentAsString).isEqualTo("<div>Hello John</div>")

                    val helloWithAuthRequest = MockMvcRequestBuilders.get("/helloauth/John")
                        .accept(MediaType.TEXT_HTML)
                    val helloWithAuthResponse = perform(helloWithAuthRequest).andReturn().response
                    assertThat(helloWithAuthResponse).isNotNull()
                    assertThat(helloWithAuthResponse.status).isEqualTo(200)
                    assertThat(helloWithAuthResponse.contentAsString).isEqualTo("<div>Hello John</div>")

                    val helloWithAuthOnlyRequest = MockMvcRequestBuilders.get("/helloauth")
                        .accept(MediaType.TEXT_HTML)
                    val helloWithAuthOnlyResponse = perform(helloWithAuthOnlyRequest).andReturn().response
                    assertThat(helloWithAuthOnlyResponse).isNotNull()
                    assertThat(helloWithAuthOnlyResponse.status).isEqualTo(200)
                    assertThat(helloWithAuthOnlyResponse.contentAsString).isEqualTo("<div>Hello anonymous</div>")
                }
            }
        }
    }

    @JvmRecord
    data class Request(val name: String)
    class Controller {
        fun hello(req: Request): String = "Hello ${req.name}"
        fun sayHelloWithAuth(user: Principal?, request: Request): String {
            return if (user?.name != null) {
                "Hello ${user.name}"
            } else {
                "Hello ${request.name}"
            }
        }

        fun sayHelloWithAuthOnly(user: Principal?): String {
            return "Hello ${user?.name ?: "anonymous"}"
        }

        fun getList(): List<String> {
            return emptyList()
        }
    }
}