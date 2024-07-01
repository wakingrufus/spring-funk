package com.github.wakingrufus.springdsl.aws.s3

import com.github.wakingrufus.funk.aws.aws
import com.github.wakingrufus.funk.aws.s3.S3ClientInitializer
import com.github.wakingrufus.funk.beans.BeanDslInitializer
import com.github.wakingrufus.funk.beans.beans
import com.github.wakingrufus.springdsl.test.testDslApplication
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.getBean
import org.springframework.beans.factory.getBeanProvider
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.CreateBucketRequest

@Testcontainers
class S3ClientInitializerTest {
    companion object {

        @Container
        val localStackContainer: LocalStackContainer =
            LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
                .withServices(LocalStackContainer.Service.S3)

    }

    @Test
    fun `test disabled`() {
        testDslApplication(S3ClientInitializer()) {
            application {
                aws {
                    s3 {
                        client("default")
                    }
                }
            }
            environment {
                setProperty("awssdk.global.enabled", "false")
            }
            test {
                val client: S3AsyncClient? = getBeanProvider<S3AsyncClient>().ifAvailable
                assertThat(client).isNull()
            }
        }
    }

    @Test
    fun `test no MeterRegistry`() {
        testDslApplication(S3ClientInitializer()) {
            application {
                aws {
                    s3 {
                        client("default")
                    }
                }
            }
            environment {
                setProperty("awssdk.s3.default.region", "us-west-2") // test env might not have AWS setup
                setProperty("awssdk.s3.default.accessKeyId", "test") // test env might not have AWS setup
                setProperty("awssdk.s3.default.secretAccessKey", "test") // test env might not have AWS setup
                setProperty(
                    "awssdk.global.s3EndpointOverride",
                    localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3).toString()
                )
            }
            test {
                val client = getBean<S3AsyncClient>()
                val createResponse = client.createBucket(CreateBucketRequest.builder().bucket("bucket").build()).join()
                assertThat(createResponse.sdkHttpResponse().isSuccessful).isTrue
            }
        }
    }

    @Test
    fun `test with metrics`() {
        testDslApplication(BeanDslInitializer(), S3ClientInitializer()) {
            application {
                aws {
                    s3 {
                        client("default")
                    }
                }
                beans {
                    bean<MeterRegistry> { SimpleMeterRegistry() }
                }
            }
            environment {
                setProperty("awssdk.s3.default.region", "us-west-2") // test env might not have AWS setup
                setProperty("awssdk.s3.default.accessKeyId", "test") // test env might not have AWS setup
                setProperty("awssdk.s3.default.secretAccessKey", "test") // test env might not have AWS setup
                setProperty(
                    "awssdk.global.s3EndpointOverride",
                    localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3).toString()
                )
            }
            test {
                val client = getBean<S3AsyncClient>()
                val createResponse =
                    client.createBucket(CreateBucketRequest.builder().bucket("bucket-with-metrics").build()).join()
                assertThat(createResponse.sdkHttpResponse().isSuccessful).isTrue

                val meterRegistry = getBean<SimpleMeterRegistry>()
                assertThat(
                    meterRegistry.timer(
                        "awssdk",
                        listOf(
                            Tag.of("name", "default"),
                            Tag.of("operation", "CreateBucket"),
                            Tag.of("outcome", "Success"),
                            Tag.of("service", "S3")
                        )
                    ).count()
                ).isEqualTo(1L)
            }
        }
    }

    @Test
    fun `test multiple clients`() {
        testDslApplication(BeanDslInitializer(), S3ClientInitializer()) {
            application {
                aws {
                    s3 {
                        client("default")
                        client("east") {
                            defaults {

                            }
                            overrideConfig {

                            }
                            customization {
                                region(Region.US_EAST_1)
                            }
                        }
                    }
                }
                beans {
                    bean<MeterRegistry> { SimpleMeterRegistry() }
                }
            }
            environment {
                setProperty("awssdk.s3.default.region", "us-west-2") // test env might not have AWS setup
                setProperty("awssdk.s3.default.accessKeyId", "test") // test env might not have AWS setup
                setProperty("awssdk.s3.default.secretAccessKey", "test") // test env might not have AWS setup
                setProperty("awssdk.s3.east.region", "us-east-1") // test env might not have AWS setup
                setProperty("awssdk.s3.east.accessKeyId", "test") // test env might not have AWS setup
                setProperty("awssdk.s3.east.secretAccessKey", "test") // test env might not have AWS setup
                setProperty(
                    "awssdk.global.s3EndpointOverride",
                    localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3).toString()
                )
            }
            test {
                val meterRegistry = getBean<SimpleMeterRegistry>()

                val client = getBean<S3AsyncClient>("s3-default")
                val createResponse =
                    client.createBucket(CreateBucketRequest.builder().bucket("default-bucket").build()).join()
                assertThat(createResponse.sdkHttpResponse().isSuccessful).isTrue
                assertThat(
                    meterRegistry.timer(
                        "awssdk",
                        listOf(
                            Tag.of("name", "default"),
                            Tag.of("operation", "CreateBucket"),
                            Tag.of("outcome", "Success"),
                            Tag.of("service", "S3")
                        )
                    ).count()
                ).isEqualTo(1L)

                val eastClient = getBean<S3AsyncClient>("s3-east")
                val eastCreateResponse =
                    eastClient.createBucket(CreateBucketRequest.builder().bucket("east-bucket").build()).join()
                assertThat(eastCreateResponse.sdkHttpResponse().isSuccessful).isTrue
                assertThat(
                    meterRegistry.timer(
                        "awssdk",
                        listOf(
                            Tag.of("name", "east"),
                            Tag.of("operation", "CreateBucket"),
                            Tag.of("outcome", "Success"),
                            Tag.of("service", "S3")
                        )
                    ).count()
                ).isEqualTo(1L)
            }
        }
    }
}
