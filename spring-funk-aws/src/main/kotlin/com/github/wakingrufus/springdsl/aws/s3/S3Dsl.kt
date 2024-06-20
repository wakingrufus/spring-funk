package com.github.wakingrufus.springdsl.aws.s3

import com.github.wakingrufus.funk.core.SpringDslMarker
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder

@SpringDslMarker
class S3Dsl {
    internal val clientMap: MutableMap<String, S3ClientDsl> = mutableMapOf()

    @SpringDslMarker
    fun client(name: String, builder: S3ClientDsl.() -> Unit = {}) {
        clientMap[name] = S3ClientDsl().apply(builder)
    }

}

@SpringDslMarker
class S3ClientDsl {
    internal var config: S3ClientConfigurationProperties = S3ClientConfigurationProperties()
    internal var customization: S3AsyncClientBuilder.() -> Unit = {}
    internal var overrideConfig: ClientOverrideConfiguration.Builder.() -> Unit = {}

    @SpringDslMarker
    fun defaults(builder: S3ClientConfigurationProperties.() -> Unit) {
        config.apply(builder)
    }

    @SpringDslMarker
    fun customization(builder: S3AsyncClientBuilder.() -> Unit) {
        customization = builder
    }

    @SpringDslMarker
    fun overrideConfig(builder: ClientOverrideConfiguration.Builder.() -> Unit) {
        overrideConfig = builder
    }
}