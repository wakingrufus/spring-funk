package com.github.wakingrufus.funk

import io.github.oshai.kotlinlogging.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.context.environment.EnvironmentManager
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    classes = [TestApplication::class],
    properties = [
        "version=1.0.1",
        "spring.application.name=TEST",
        "prefix.figKey=startup"
    ]
)
class IntegrationTest {
    private val log = KotlinLogging.logger {}

    @Autowired
    lateinit var environmentManager: EnvironmentManager

    @Autowired
    lateinit var config: RuntimeConfig<ConfigProps>

    @Test
    fun test_runtimeConfigs() {
        assertThat(config.get().figKey).isEqualTo("startup")
        environmentManager.setProperty("prefix.figKey", "new")
        log.info { "new fig set" }
        assertThat(config.get().figKey).isEqualTo("new")
    }
}
