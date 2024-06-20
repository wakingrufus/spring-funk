package com.github.wakingrufus.springdsl.aws

import com.github.wakingrufus.springdsl.aws.s3.S3Dsl
import com.github.wakingrufus.funk.core.SpringDsl
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.core.SpringDslMarker

@SpringDslMarker
class AwsDsl : SpringDsl {
    internal val s3Dsl = S3Dsl()

    @SpringDslMarker
    fun s3(config: S3Dsl.() -> Unit) {
        s3Dsl.apply(config)
    }
}

@SpringDslMarker
fun SpringDslContainer.aws(config: AwsDsl.() -> Unit) {
    register(AwsDsl().apply(config))
}