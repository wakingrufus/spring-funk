package com.github.wakingrufus.funk

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestApplication::class])
class TestIntegrationTest {
    @Autowired
    val stringbean: String? = null

    @Autowired
    val bean: String? = null

    @Test
    fun test() {
        assertThat(stringbean).isEqualTo("1")
        assertThat(bean).isEqualTo("2")
    }
}
