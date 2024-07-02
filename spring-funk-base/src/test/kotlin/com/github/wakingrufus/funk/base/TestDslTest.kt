package com.github.wakingrufus.funk.base

import com.github.wakingrufus.funk.test.testDslApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * An example of how to test DSL-enabled initializers in Kotlin
 */
class TestDslTest {

    @Test
    fun test() {
        testDslApplication(TestDslInitializer()) {
            application {
                testDsl {
                    stringBean("testa", "1")
                    stringBean("testb", "2")
                }
            }
            environment {
            }
            test {
                assertThat(getBean("testa")).isEqualTo("1")
                assertThat(getBean("testb")).isEqualTo("2")
            }
        }
    }
}