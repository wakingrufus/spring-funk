package com.github.wakingrufus.springdsl.runtimeconfig

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.mock.env.MockEnvironment

internal class SpringRuntimeConfigTest {
    @Test
    fun test() {
        val environment = MockEnvironment()
        val boundConfig = SpringRuntimeConfig.create(ConfigClassNoAnnotation::class.java, environment, "prefix")
        assertThat(boundConfig.get().key).isNull()
        environment.setProperty("prefix.key", "new")
        boundConfig.update()
        assertThat(boundConfig.get().key).isEqualTo("new")
    }

    @Test
    fun test_annotation() {
        val environment = MockEnvironment()
        val boundConfig = SpringRuntimeConfig.create(ConfigClassWithAnnotation::class.java, environment)
        assertThat(boundConfig.get().figKey).isNull()
        environment.setProperty("prefix2.figKey", "new")
        boundConfig.update()
        assertThat(boundConfig.get().figKey).isEqualTo("new")
    }

    @Test
    fun test_immutable_annotation() {
        val environment = MockEnvironment()
        val boundConfig = SpringRuntimeConfig.create(ImmutableConfigClassWithAnnotation::class.java, environment)
        assertThat(boundConfig.get().figKey).isNull()
        environment.setProperty("immutable.figKey", "new")
        boundConfig.update()
        assertThat(boundConfig.get().figKey).isEqualTo("new")
    }

    @Test
    fun test_immutable() {
        val environment = MockEnvironment()
        val boundConfig = SpringRuntimeConfig.create(ImmutableConfigClassNoAnnotation::class.java, environment, "prefix")
        assertThat(boundConfig.get().key).isNull()
        environment.setProperty("prefix.key", "new")
        boundConfig.update()
        assertThat(boundConfig.get().key).isEqualTo("new")
    }

}

data class ConfigClassNoAnnotation(var key: String?)

@ConfigurationProperties(prefix = "prefix2")
data class ConfigClassWithAnnotation(var figKey: String?)

data class ImmutableConfigClassNoAnnotation(val key: String?)

@ConfigurationProperties(prefix = "immutable")
data class ImmutableConfigClassWithAnnotation(val figKey: String?)