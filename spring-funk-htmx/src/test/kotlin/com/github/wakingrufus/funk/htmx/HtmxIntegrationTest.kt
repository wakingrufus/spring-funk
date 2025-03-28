package com.github.wakingrufus.funk.htmx

import com.github.wakingrufus.funk.htmx.swap.HxSwapType
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.serialize
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.script
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HtmxIntegrationTest {

    @Test
    fun test() {
        val expected = """<!DOCTYPE html>
<html><head><META http-equiv="Content-Type" content="text/html; charset=UTF-8"><script src="https://unpkg.com/htmx.org@2.0.4"></script></head><body><div></div><button hx-post="/load" hx-swap="outerHTML">Click Me</button></body></html>"""
        val actual = createHTMLDocument().html {
            head {
                script {
                    src = "https://unpkg.com/htmx.org@2.0.4"
                }
            }
            body {
                div { }
                button {
                    hxPost("/load"){
                        swap(HxSwapType.OuterHtml)
                    }
                    +"Click Me"
                }
            }
        }.serialize(false)
        assertThat(actual).isEqualTo(expected)
    }
}