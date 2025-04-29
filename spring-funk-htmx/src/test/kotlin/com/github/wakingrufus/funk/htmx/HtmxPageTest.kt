package com.github.wakingrufus.funk.htmx

import com.github.wakingrufus.funk.htmx.template.htmxTemplate
import kotlinx.html.span
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class HtmxPageTest {
    @JvmRecord
    data class TestRequestClass(val id: UUID)

    @JvmRecord
    data class TestResponseClass(val id: UUID)
    class TestController {
        fun noParamGet(): TestResponseClass {
            return TestResponseClass(UUID.randomUUID())
        }

        fun get(req: TestRequestClass): TestResponseClass {
            return TestResponseClass(req.id)
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
}