package com.github.wakingrufus.funk.htmx

import com.github.wakingrufus.funk.beans.BeanDslInitializer
import com.github.wakingrufus.funk.beans.beans
import com.github.wakingrufus.funk.test.testDslApplication
import com.github.wakingrufus.funk.webmvc.WebmvcInitializer
import com.github.wakingrufus.funk.webmvc.webmvc
import kotlinx.html.div
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import java.security.Principal

internal class IntegrationTest {

    @Test
    fun test() {
        testDslApplication(HtmxInitializer(), WebmvcInitializer(), BeanDslInitializer()) {
            environment {
                setProperty("server.port", "0")
            }
            webApplication {
                beans {
                    bean<Controller>()
                }
                webmvc {
                    enableWebMvc {
                        jetty()
                    }
                    converters {
                        jackson()
                        form()
                        string()
                    }
                }
                htmx {
                    page("/index") {
                        initialLoad {
                            div {

                            }
                        }
                        route(HttpVerb.GET, "/hello/{name}", Controller::hello) {
                            div { +it }
                        }
                        route(HttpVerb.GET, "/hello/{name}", Controller::sayHelloWithAuth) {
                            div { +it }
                        }
                    }
                }
            }
            test {
                assertThat(testRestTemplate).isNotNull
                testRestTemplate!!.run {
                    val indexRequest = RequestEntity.get("/index")
                        .accept(MediaType.TEXT_HTML)
                        .build()
                    val indexResponse = exchange(indexRequest, String::class.java)
                    assertThat(indexResponse).isNotNull()
                    assertThat(indexResponse.statusCode.value()).isEqualTo(200)

                    val helloRequest = RequestEntity.get("/hello/John")
                        .accept(MediaType.TEXT_HTML)
                        .build()
                    val helloResponse = exchange(helloRequest, String::class.java)
                    assertThat(helloResponse).isNotNull()
                    assertThat(helloResponse.statusCode.value()).isEqualTo(200)
                    assertThat(helloResponse.body).isEqualTo("<div>Hello John</div>")

                    val helloWithAuthRequest = RequestEntity.get("/hello/John")
                        .accept(MediaType.TEXT_HTML)
                        .build()
                    val helloWithAuthResponse = exchange(helloWithAuthRequest, String::class.java)
                    assertThat(helloWithAuthResponse).isNotNull()
                    assertThat(helloWithAuthResponse.statusCode.value()).isEqualTo(200)
                    assertThat(helloWithAuthResponse.body).isEqualTo("<div>Hello John</div>")
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
    }
}