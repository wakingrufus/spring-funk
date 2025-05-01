package com.github.wakingrufus.funk.htmx.route

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.wakingrufus.funk.htmx.template.htmxTemplate
import kotlinx.html.div
import kotlinx.html.span
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.function.RouterFunctionDsl
import org.springframework.web.servlet.function.router
import java.util.UUID

class HxRouteTest {
    @JvmRecord
    data class RequestObj(val id: UUID, val name: String)
    class Controller {
        fun action(request: RequestObj): RequestObj {
            return request
        }
    }

    @Test
    fun test() {
        val context = GenericApplicationContext()
        context.registerBean<Controller>()
        context.registerBean<ObjectMapper> { ObjectMapper() }
        context.refresh()
        val mockMvc = MockMvcBuilders.routerFunctions(
            router {
                withParam(
                    RouterFunctionDsl::POST, "/index/{id}", RequestObj::class.java, Controller::class.java,
                    Controller::action,
                    htmxTemplate {
                        div {
                            span {
                                +it.id.toString()
                            }
                            span {
                                +it.name
                            }
                        }
                    }).registerRoutes(context, this)
            }
        ).build()
        val id = UUID.randomUUID()
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/index/$id")
                .param("name", "test name")
        ).andReturn()
        assertThat(result.response.status).isEqualTo(200)
        assertThat(result.response.contentAsString)
            .`as`("path params and query params are combined for deserialization")
            .isEqualTo("""<div><span>$id</span><span>test name</span></div>""")
        context.close()
    }
}