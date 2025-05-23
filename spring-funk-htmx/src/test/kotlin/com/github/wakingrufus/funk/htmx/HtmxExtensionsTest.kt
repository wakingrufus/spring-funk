package com.github.wakingrufus.funk.htmx

import kotlinx.html.span
import kotlinx.html.stream.appendHTML
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class HtmxExtensionsTest {
    @Test
    fun `test hxGet`() {
        val actual = buildString {
            appendHTML(false).apply {
                span {
                    hxGet("/path")
                }
            }
        }
        assertThat(actual).isEqualTo("""<span hx-get="/path"></span>""")
    }

    @Test
    fun `test hxPost`() {
        val actual = buildString {
            appendHTML(false).apply {
                span {
                    hxPost("/path")
                }
            }
        }
        assertThat(actual).isEqualTo("""<span hx-post="/path"></span>""")
    }

    @Test
    fun `test hxPut`() {
        val actual = buildString {
            appendHTML(false).apply {
                span {
                    hxPut("/path")
                }
            }
        }
        assertThat(actual).isEqualTo("""<span hx-put="/path"></span>""")
    }

    @Test
    fun `test hxPatch`() {
        val actual = buildString {
            appendHTML(false).apply {
                span {
                    hxPatch("/path")
                }
            }
        }
        assertThat(actual).isEqualTo("""<span hx-patch="/path"></span>""")
    }

    @Test
    fun `test hxDelete`() {
        val actual = buildString {
            appendHTML(false).apply {
                span {
                    hxDelete("/path")
                }
            }
        }
        assertThat(actual).isEqualTo("""<span hx-delete="/path"></span>""")
    }
}