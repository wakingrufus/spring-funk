package com.github.wakingrufus.funk.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.support.GenericApplicationContext
import org.springframework.mock.env.MockEnvironment

internal class RuntimeConfigUtilTest {
    lateinit var context: GenericApplicationContext
    lateinit var environment: MockEnvironment

    @BeforeEach
    fun setup() {
        context = GenericApplicationContext()
        environment = MockEnvironment()
        context.environment = environment
    }

    @AfterEach
    fun close() {
        context.close()
    }

    @Test
    fun `test missing`() {
        context.refresh()
        val result = context.getRuntimeConfig<ConfigProps>()
        assertThat(result).isNull()
    }

    @Test
    fun `test no props`() {
        context.registerRuntimeConfig<NullableConfigProps>()
        context.refresh()
        val result = context.getRuntimeConfig<NullableConfigProps>()
        assertThat(result).isNotNull()
    }

    @Test
    fun test() {
        environment.setProperty("prefix.configKey", "test")
        context.registerRuntimeConfig<ConfigProps>()
        context.refresh()
        val result = context.getRuntimeConfig<ConfigProps>()
        assertThat(result).isNotNull()
        assertThat(result?.configKey).isEqualTo("test")
    }

    @Test
    fun `test 2 beans`() {
        environment.setProperty("prefixA.configKey", "a")
        environment.setProperty("prefixB.configKey", "b")
        context.registerRuntimeConfig<ConfigProps>("prefixA")
        context.registerRuntimeConfig<ConfigProps>("prefix-b")
        context.refresh()
        val resultA = context.getRuntimeConfig<ConfigProps>("prefixA")
        assertThat(resultA).isNotNull()
        assertThat(resultA?.configKey).isEqualTo("a")
        val resultB = context.getRuntimeConfig<ConfigProps>("prefixB")
        assertThat(resultB).isNotNull()
        assertThat(resultB?.configKey).isEqualTo("b")
    }

    @Test
    fun `test 2 beans typed`() {
        environment.setProperty("prefix.configKey", "a")
        environment.setProperty("prefix2.configKey", "b")
        context.registerRuntimeConfig<ConfigProps>()
        context.registerRuntimeConfig<ConfigProps2>()
        context.refresh()
        val resultA = context.getRuntimeConfig<ConfigProps>()
        assertThat(resultA).isNotNull()
        assertThat(resultA?.configKey).isEqualTo("a")
        val resultB = context.getRuntimeConfig<ConfigProps2>()
        assertThat(resultB).isNotNull()
        assertThat(resultB?.configKey).isEqualTo("b")
    }
}
