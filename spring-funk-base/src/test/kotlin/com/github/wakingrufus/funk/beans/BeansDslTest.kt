package com.github.wakingrufus.funk.beans

import com.github.wakingrufus.funk.test.testDslApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * An example of how to test DSL-enabled initializers in Kotlin
 */
class BeansDslTest {

    @Test
    fun test() {
        testDslApplication(BeanDslInitializer()) {
            application {
                beans {
                    bean<String>("testa") { "1" }
                }
            }
            environment {
            }
            test {
                assertThat(getBean("testa")).isEqualTo("1")
            }
        }
    }
}