package com.github.wakingrufus.funk.webmvc

import com.github.wakingrufus.funk.test.testDslApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.getBeanProvider
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.web.servlet.function.ServerResponse

class WebmvcInitializerTest {
    @Test
    fun `test jetty`() {
        testDslApplication(WebmvcInitializer()) {
            webApplication {
                webmvc {
                    enableWebMvc {
                        jetty()
                    }
                    routes {
                        route {
                            GET("/index") {
                                ServerResponse.ok().build()
                            }
                        }
                    }
                }
            }
            environment {
                setProperty("server.port", "0")
            }
            test {
                val factory = getBeanProvider<JettyServletWebServerFactory>().first()
                assertThat(factory).isNotNull
                assertThat(factory.compression!!.enabled).isFalse
                assertThat(factory.http2!!.isEnabled).isFalse
                assertThat(factory.ssl).isNull()

                val response = testRestTemplate!!.getForEntity("/index", String::class.java)
                assertThat(response.statusCode.value()).isEqualTo(200)
            }
        }
    }

    @Test
    fun `test tomcat`() {
        testDslApplication(WebmvcInitializer()) {
            webApplication {
                webmvc {
                    enableWebMvc {
                        tomcat()
                    }
                    routes {
                        route {
                            GET("/index") {
                                ServerResponse.ok().build()
                            }
                        }
                    }
                }
            }
            environment {
                setProperty("server.port", "0")
            }
            test {
                val factory = getBeanProvider<TomcatServletWebServerFactory>().first()
                assertThat(factory).isNotNull
                assertThat(factory.compression!!.enabled).isFalse
                assertThat(factory.http2!!.isEnabled).isFalse
                assertThat(factory.ssl).isNull()

                val response = testRestTemplate!!.getForEntity("/index", String::class.java)
                assertThat(response.statusCode.value()).isEqualTo(200)
            }
        }
    }
}