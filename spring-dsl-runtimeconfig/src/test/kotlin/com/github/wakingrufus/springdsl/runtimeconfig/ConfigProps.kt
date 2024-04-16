package com.github.wakingrufus.springdsl.runtimeconfig

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "prefix")
data class ConfigProps(val figKey: String, val nullableFigKey: String?)
