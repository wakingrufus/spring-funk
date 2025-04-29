package com.github.wakingrufus.funk.htmx.template

import kotlinx.html.body
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.serialize
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.span
import kotlinx.html.stream.appendHTML
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HtmxTemplateTest {
    @Test
    fun `test fragment`() {
        val template = htmxTemplate<String> {
            span {
                +it
            }
        }
        val output = buildString {
            appendHTML(false).apply {
                template.render(this, "Hello")
            }
        }
        assertThat(output).isEqualTo("<span>Hello</span>")
    }

    @Test
    fun `test document`() {
        val template = htmxTemplate<String> {
            span {
                +it
            }
        }

        val output = createHTMLDocument().html {
            head {
            }
            body {
                template.render(consumer, "Hello")
            }
        }.serialize(false)
        assertThat(output)
            .isEqualTo(
                "<!DOCTYPE html>\n" +
                        "<html>" +
                        "<head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head>" +
                        "<body><span>Hello</span></body>" +
                        "</html>"
            )
    }
}
