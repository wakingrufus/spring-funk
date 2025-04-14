package com.github.wakingrufus.funk.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "prefix3")
data class NullableConfigProps(val nullableConfigKey: String?)
