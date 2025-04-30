package com.github.wakingrufus.funk.htmx

import kotlinx.html.div
import kotlinx.html.stream.appendHTML
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TriggerDslTest {
    @Test
    fun `test default params`() {
        val actual = buildString {
            appendHTML(false).div {
                TriggerDsl(HttpVerb.GET, "/")(this)
            }
        }
        assertThat(actual).isEqualTo("""<div hx-get="/"></div>""")
    }

    @Test
    fun `test no params`() {
        val actual = buildString {
            appendHTML(false).div {
                TriggerDsl(HttpVerb.GET, "/").apply {
                    noParams()
                }(this)
            }
        }
        assertThat(actual).isEqualTo("""<div hx-get="/" hx-params="none"></div>""")
    }

    @Test
    fun `test all params`() {
        val actual = buildString {
            appendHTML(false).div {
                TriggerDsl(HttpVerb.GET, "/").apply {
                    allParams()
                }(this)
            }
        }
        assertThat(actual).isEqualTo("""<div hx-get="/" hx-params="*"></div>""")
    }

    @Test
    fun `test include params`() {
        val actual = buildString {
            appendHTML(false).div {
                TriggerDsl(HttpVerb.GET, "/").apply {
                    includeParams("1", "2")
                }(this)
            }
        }
        assertThat(actual).isEqualTo("""<div hx-get="/" hx-params="1,2"></div>""")
    }

    @Test
    fun `test exclude params`() {
        val actual = buildString {
            appendHTML(false).div {
                TriggerDsl(HttpVerb.GET, "/").apply {
                    excludeParams("1", "2")
                }(this)
            }
        }
        assertThat(actual).isEqualTo("""<div hx-get="/" hx-params="not 1,2"></div>""")
    }

    @Test
    fun `test target`() {
        val actual = buildString {
            appendHTML(false).div {
                TriggerDsl(HttpVerb.GET, "/").apply {
                    target = "body"
                }(this)
            }
        }
        assertThat(actual).isEqualTo("""<div hx-get="/" hx-target="body"></div>""")
    }
}