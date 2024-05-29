package com.github.wakingrufus.springdsl.aws.s3

import java.time.Duration

class S3ClientConfigurationProperties(
    var endpointOverride: String? = null,
    var maxConcurrency: Int = 1,
    var region: String? = null,
    var accessKeyId: String? = null,
    var secretAccessKey: String? = null,
    var connectionTimeout: Duration? = null,
    var readWriteTimeout: Duration? = null
)
