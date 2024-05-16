package com.github.wakingrufus.springdsl.webmvc

import com.github.wakingrufus.springdsl.test.testDslApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.getBeanProvider
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory

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
}