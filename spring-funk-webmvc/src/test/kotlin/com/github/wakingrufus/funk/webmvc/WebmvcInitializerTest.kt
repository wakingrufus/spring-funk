package com.github.wakingrufus.funk.webmvc

import com.github.wakingrufus.funk.test.testDslApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.getBeanProvider
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory

class WebmvcInitializerTest {
    @Test
    fun `test jetty`() {
        testDslApplication(WebmvcInitializer()) {
            webApplication {
                webmvc {
                    enableWebMvc {
                        jetty()
                    }
                }
            }
            environment {

            }
            test {
                assertThat(getBeanProvider<JettyServletWebServerFactory>().firstOrNull()).isNotNull
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
                }
            }
            environment {

            }
            test {
                assertThat(getBeanProvider<TomcatServletWebServerFactory>().firstOrNull()).isNotNull
            }
        }
    }
}