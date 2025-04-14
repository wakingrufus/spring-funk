package com.github.wakingrufus.funk.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "prefix2")
data class ConfigProps2(val configKey: String, val nullableConfigKey: String?)
