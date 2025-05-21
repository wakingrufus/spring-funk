package com.github.wakingrufus.funk.webmvc.security

import com.github.wakingrufus.funk.test.testClient
import com.github.wakingrufus.funk.test.testDslApplication
import com.github.wakingrufus.funk.webmvc.WebmvcInitializer
import com.github.wakingrufus.funk.webmvc.webmvc
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Test
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRepository
import org.springframework.security.web.csrf.DefaultCsrfToken
import org.springframework.test.web.reactive.server.WebTestClient
import java.nio.charset.Charset
import java.util.Base64

const val usernameTest = "user"
const val passwordTest = "password"

// for CSRF
const val csrfHeaderTest = "CSRF_HEADER"
const val csrfTokenTest = "test_csrf_token"
const val csrfSessionAttribute = "CSRF_ATTRIBUTE"

internal class FunkWebMvcSecurityInitializerTest {
    @Test
    fun test() {
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
                val client = testClient()

                commonTests(client)

                // Verify post succeed with csrf header
                client
                    .post().uri("/public-post")
                    .header(csrfHeaderTest, csrfTokenTest)
                    .exchange()
                    .expectStatus().is2xxSuccessful
            }
        }
    }


    private fun userDetailsService() =
        InMemoryUserDetailsManager(
            @Suppress("DEPRECATION")
            User.withDefaultPasswordEncoder()
                .username(usernameTest)
                .password(passwordTest)
                .roles("USER")
                .build()
        )

    private fun csrfTokenRepository() = object : CsrfTokenRepository {

        override fun generateToken(request: HttpServletRequest) =
            DefaultCsrfToken(csrfHeaderTest, "not_used", csrfTokenTest)

        override fun saveToken(token: CsrfToken?, request: HttpServletRequest, response: HttpServletResponse) {
            if (token == null) {
                request.getSession(false).removeAttribute(csrfSessionAttribute)
            } else {
                request.session.setAttribute(csrfSessionAttribute, token)
            }
        }

        override fun loadToken(request: HttpServletRequest): CsrfToken? {
            val session = request.getSession(false) ?: return null
            return (session.getAttribute(csrfSessionAttribute) as CsrfToken)
        }
    }

    private fun commonTests(client: WebTestClient) = client.apply {
        get().uri("/public-view")
            .exchange()
            .expectStatus().is2xxSuccessful

        // Verify post fails without csrf header
        post().uri("/public-post")
            .exchange()
            .expectStatus().isForbidden

        get().uri("/view").exchange()
            .expectStatus().isUnauthorized

        val basicAuth =
            Base64.getEncoder().encode("$usernameTest:$passwordTest".toByteArray()).toString(Charset.defaultCharset())
        get().uri("/view").header("Authorization", "Basic $basicAuth").exchange()
            .expectStatus().is2xxSuccessful

        val basicAuthWrong =
            Base64.getEncoder().encode("user:pass".toByteArray()).toString(Charset.defaultCharset())
        get().uri("/view").header("Authorization", "Basic $basicAuthWrong").exchange()
            .expectStatus().isUnauthorized
    }
}
