import com.github.wakingrufus.funk.aws.aws
import com.github.wakingrufus.funk.aws.s3.S3ClientInitializer
import com.github.wakingrufus.funk.test.testDslApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.getBean
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
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

    /**
     * Tests that everything works when micrometer is not on the classpath
     */
    @Test
    fun `test no metrics`() {
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
}