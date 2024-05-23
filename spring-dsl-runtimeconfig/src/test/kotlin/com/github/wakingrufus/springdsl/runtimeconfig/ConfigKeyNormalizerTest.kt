package com.github.wakingrufus.springdsl.runtimeconfig

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ConfigKeyNormalizerTest {
    @Test
    fun test_kebab() {
        assertThat(normalizeConfigKey("test-kebab"))
            .isEqualTo("test-kebab")
    }

    @Test
    fun test_camel() {
        assertThat(normalizeConfigKey("testCamel"))
            .isEqualTo("test-camel")
    }

    @Test
    fun test_snake() {
        assertThat(normalizeConfigKey("test_snake"))
            .isEqualTo("test-snake")
    }

    @Test
    fun test_mix() {
        assertThat(normalizeConfigKey("test_snake-kebabCamel"))
            .isEqualTo("test-snake-kebab-camel")
    }
}
