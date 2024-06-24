package com.github.wakingrufus.funk

import com.github.wakingrufus.springdsl.test.testDslApplication
import io.github.oshai.kotlinlogging.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.getBean

class RuntimeConfigDslInitializerTest {
    private val log = KotlinLogging.logger {}

    @Test
    fun test() {
        testDslApplication(RuntimeConfigDslInitializer()) {
            application {
                runtimeConfig {
                    registerConfigClass(ConfigProps::class.java)
                }
            }
            environment {
                setProperty("prefix.figKey", "startup")
            }
            test {
                getBeansOfType(RuntimeConfig::class.java).forEach { t, u ->
                    log.info { t + u.toString() + u.get().toString() }
                }
                val configBean = getBean<RuntimeConfig<ConfigProps>>()
                assertThat(configBean).isNotNull
                assertThat(configBean.get().figKey).isEqualTo("startup")
            }
        }
    }
}
