package com.github.wakingrufus.springdsl.aws.s3

import io.netty.handler.ssl.SslProvider
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder

internal fun newClientBuilder(
    clientConfig: S3ClientConfigurationProperties
): S3AsyncClientBuilder {
    val awsBuilder = S3AsyncClient.builder()
    val builder: NettyNioAsyncHttpClient.Builder =
        NettyNioAsyncHttpClient.builder()
            .sslProvider(SslProvider.OPENSSL)
            .maxConcurrency(clientConfig.maxConcurrency)
            .connectionAcquisitionTimeout(clientConfig.connectionTimeout)
            .connectionTimeout(clientConfig.connectionTimeout)
            .readTimeout(clientConfig.readWriteTimeout)
            .writeTimeout(clientConfig.readWriteTimeout)
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
