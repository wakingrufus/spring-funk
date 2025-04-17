package com.github.wakingrufus.funk.htmx.trigger

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration

class HxTriggerTest {
    @Test
    fun `test poll`() {
        val actual = HxTriggerDsl().apply {
            poll(Duration.ofSeconds(10))
        }()
        assertThat(actual()).isEqualTo("every 10s")
    }


    @Test
    fun `test standard event`() {
        val actual = HxTriggerDsl().apply {
            event("click") {
                once()
            }
        }()
        assertThat(actual()).isEqualTo("click once")
    }

    @Test
    fun `test intersect event`() {
        val actual = HxTriggerDsl().apply {
            intersect {
                root("#home")
            }
        }()
        assertThat(actual()).isEqualTo("intersect root:#home")
    }
}