package com.github.wakingrufus.springdsl.aws.s3

import java.time.Duration
import java.time.temporal.ChronoUnit

class S3ClientConfigurationProperties(
    var enabled: Boolean = true,
    var endpointOverride: String? = null,
    var maxConcurrency: Int = 1,
    var region: String? = null,
    var accessKeyId: String? = null,
    var secretAccessKey: String? = null,
    var connectionTimeout: Duration = Duration.of(1, ChronoUnit.SECONDS),
    var readWriteTimeout: Duration = Duration.of(1, ChronoUnit.SECONDS)
)
