package com.github.wakingrufus.funk.aws

import com.github.wakingrufus.funk.aws.GlobalAwsConfigurationProperties.Companion.PREFIX
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(PREFIX)
class GlobalAwsConfigurationProperties(
    /**
     * Allow endpoint to be overridden for all clients. Useful for pointing clients to localstack in integration tests
     */
    var s3EndpointOverride: String? = null,
    /**
     * Allow feature to be disabled with configuration, for integration test slicing, for example
     */
    var enabled: Boolean = true
) {
    companion object {
        const val PREFIX = "awssdk.global"
    }
}