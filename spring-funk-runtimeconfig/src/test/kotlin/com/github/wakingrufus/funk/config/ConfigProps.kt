package com.github.wakingrufus.funk.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "prefix")
data class ConfigProps(val figKey: String, val nullableFigKey: String?)
