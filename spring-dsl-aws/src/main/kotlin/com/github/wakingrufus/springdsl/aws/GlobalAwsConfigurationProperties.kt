package com.github.wakingrufus.springdsl.aws

import com.github.wakingrufus.springdsl.aws.GlobalAwsConfigurationProperties.Companion.PREFIX
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(PREFIX)
class GlobalAwsConfigurationProperties(var s3EndpointOverride: String? = null) {
    companion object {
        const val PREFIX = "awssdk.global"
    }
}