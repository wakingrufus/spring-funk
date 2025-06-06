package com.github.wakingrufus.funk.aws.s3

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.http.crt.AwsCrtAsyncHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder

internal fun newClientBuilder(clientConfig: S3ClientConfigurationProperties): S3AsyncClientBuilder {
    val awsBuilder = S3AsyncClient.builder()
    val builder = AwsCrtAsyncHttpClient.builder()
            .maxConcurrency(clientConfig.maxConcurrency)
    clientConfig.readWriteTimeout?.also {
        // TODO fixme
    }
    clientConfig.connectionTimeout?.also {
        builder.connectionTimeout(it)
    }
    awsBuilder.httpClientBuilder(builder)
    clientConfig.region?.also {
        awsBuilder.region(Region.of(it))
    }

    if (clientConfig.accessKeyId != null && clientConfig.secretAccessKey != null) {
        awsBuilder.credentialsProvider {
            AwsBasicCredentials.create(clientConfig.accessKeyId, clientConfig.secretAccessKey)
        }
    } else {
        awsBuilder.credentialsProvider(
            DefaultCredentialsProvider.builder()
                .asyncCredentialUpdateEnabled(true)
                .build()
        )
    }

    return awsBuilder
}
