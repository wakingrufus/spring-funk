package com.github.wakingrufus.funk.htmx

import kotlinx.html.span
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.security.Principal
import java.util.UUID

class HtmxPageTest {
    @JvmRecord
    data class TestRequestClass(val id: UUID)

    @JvmRecord
    data class TestResponseClass(val id: UUID)

    @JvmRecord
    data class TestResponseNameClass(val name: String)
    class TestController {
        fun noParamGet(): TestResponseClass {
            return TestResponseClass(UUID.randomUUID())
        }

        fun get(req: TestRequestClass): TestResponseClass {
            return TestResponseClass(req.id)
        }

        fun auth(user: Principal?): TestResponseNameClass {
            return TestResponseNameClass(user?.name ?: "anonymous")
        }

        fun authAndParam(user: Principal?, req: TestRequestClass): TestResponseNameClass {
            return TestResponseNameClass(user?.name ?: req.id.toString())
        }
    }

    @Test
    fun `test no param get`() {
        val htmxPage = HtmxPage("").apply {
            get("", TestController::noParamGet) {
                span {
                    +it.id.toString()
                }
            }
        }
        assertThat(htmxPage.routes).hasSize(1)
    }

    @Test
    fun `test get with params`() {
        val htmxPage = HtmxPage("").apply {
            route(HttpVerb.GET, "", TestController::get) {
                span {
                    +it.id.toString()
                }
            }
        }
        assertThat(htmxPage.routes).hasSize(1)
    }

    @Test
    fun `test auth`() {
        val htmxPage = HtmxPage("").apply {
            get("", TestController::auth) {
                span {
                    +it.name
                }
            }
        }
        assertThat(htmxPage.routes).hasSize(1)
    }

    @Test
    fun `test auth and param`() {
        val htmxPage = HtmxPage("").apply {
            route(HttpVerb.GET, "", TestController::authAndParam) {
                span {
                    +it.name
                }
            }
        }
        assertThat(htmxPage.routes).hasSize(1)
    }
}