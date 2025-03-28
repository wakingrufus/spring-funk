package com.github.wakingrufus.funk.htmx.swap

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class HxSwapTest {
    @Test
    fun `test internal html settle`() {
        val actual = HxSwapDsl(HxSwapType.InnerHtml).apply {
            settle(java.time.Duration.ofSeconds(10))
        }()
        assertThat(actual()).isEqualTo("innerHTML settle:10s")
    }
}