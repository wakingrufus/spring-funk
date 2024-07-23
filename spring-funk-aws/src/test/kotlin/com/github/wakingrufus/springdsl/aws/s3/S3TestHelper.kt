package com.github.wakingrufus.springdsl.aws.s3

import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.ArrayList
import java.util.UUID
import org.apache.commons.io.IOUtils
import org.assertj.core.util.Files
import software.amazon.awssdk.core.SdkResponse
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.services.s3.S3AsyncClient

import org.assertj.core.api.Assertions.assertThat
import software.amazon.awssdk.core.async.AsyncResponseTransformer
import software.amazon.awssdk.services.s3.model.*

object S3TestHelper {
  private val BUCKET_PREFIX = "test-bucket-"
  private val PART_SIZE = 5 * 1024 * 1024
  private val NUM_PARTS = 3
  private val EXPECTED_OBJECT_SIZE: Long = (NUM_PARTS * PART_SIZE).toLong()

  fun validateClient(s3Client: S3AsyncClient) {

    val bucket = createNewBucket(s3Client)

    putObjectInBucket(s3Client, bucket)

    uploadMultiPartToBucket(s3Client, bucket)
  }

  private fun uploadMultiPartToBucket(s3Client: S3AsyncClient, bucket: String) {
    val key = "multipart-key-" + UUID.randomUUID().toString()
    val createMultipartUploadRequest =
        CreateMultipartUploadRequest.builder().bucket(bucket).key(key).build()
    val createMultipartUploadResponse =
        s3Client.createMultipartUpload(createMultipartUploadRequest).get()
    assertSuccess(createMultipartUploadResponse)

    val uploadId = createMultipartUploadResponse.uploadId()

    val completedParts = ArrayList<CompletedPart>()
    val partText = "a".repeat(PART_SIZE)

    for (part in 1..NUM_PARTS) {
      val requestBody = AsyncRequestBody.fromString(partText)
      val uploadPartRequest =
          UploadPartRequest.builder().bucket(bucket).key(key).uploadId(uploadId).partNumber(part).build()
      val uploadPartResponse = s3Client.uploadPart(uploadPartRequest, requestBody).get()
      assertSuccess(uploadPartResponse)
      completedParts.add(CompletedPart.builder().partNumber(part).eTag(uploadPartResponse.eTag()).build())
    }

    val completedMultipartUpload =
        CompletedMultipartUpload.builder().parts(completedParts).build()

    val completeMultipartUploadRequest =
        CompleteMultipartUploadRequest.builder().bucket(bucket).key(key).uploadId(uploadId)
            .multipartUpload(completedMultipartUpload).build()
    val completeMultipartUploadResponse =
        s3Client.completeMultipartUpload(completeMultipartUploadRequest).get()
    assertSuccess(completeMultipartUploadResponse)

    val localPath = Path.of(Files.temporaryFolderPath() + "/" + UUID.randomUUID().toString())
    val localFile = localPath.toFile()
    localFile.deleteOnExit()

    val getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(key).build()
    val getObjectResponse = s3Client.getObject(getObjectRequest, localFile.toPath()).get()
    assertSuccess(getObjectResponse)
    assertThat(localFile).exists()
    assertThat(localFile).hasSize(EXPECTED_OBJECT_SIZE)

    val inputStream = s3Client.getObject(getObjectRequest, AsyncResponseTransformer.toBlockingInputStream()).get()
    assertThat(inputStream).hasContent(IOUtils.toString(localFile.toURI(), StandardCharsets.UTF_8))

    val listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucket).build()
    val listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request).get()
    assertSuccess(listObjectsV2Response)
    val s3Objects = listObjectsV2Response.contents()
    assertThat(s3Objects).hasSize(2)
    val s3Object = s3Objects.filter { it.key().equals(key) }.first()
    assertThat(s3Object.key()).isEqualTo(key)
    assertThat(s3Object.eTag()).isNotNull()
  }

  private fun createNewBucket(s3Client: S3AsyncClient): String {
    val bucket = BUCKET_PREFIX + UUID.randomUUID().toString()

    val createBucketRequestBuilder = CreateBucketRequest.builder().bucket(bucket)

    val createBucketRequest = createBucketRequestBuilder.build()
    val createBucketResponse = s3Client.createBucket(createBucketRequest).get()
    assertSuccess(createBucketResponse)
    return bucket
  }

  private fun putObjectInBucket(s3Client: S3AsyncClient, bucket: String) {
    val key = "putObject-key-" + UUID.randomUUID().toString()
    val data = "Hello-" + UUID.randomUUID().toString()

    val putBuilder = PutObjectRequest.builder().bucket(bucket).key(key)
    val putResponse = s3Client.putObject(putBuilder.build(),
                    AsyncRequestBody.fromString(data)).get()
    assertSuccess(putResponse)

    val getResponse = s3Client.getObject(
      GetObjectRequest.builder()
        .bucket(bucket)
        .key(key).build(),
      AsyncResponseTransformer.toBytes())
      .get()

    assertSuccess(getResponse.response())
    assertThat(getResponse.asUtf8String()).isEqualTo(data)
  }

  private fun assertSuccess(sdkResponse: SdkResponse) {
    assertThat(sdkResponse.sdkHttpResponse().isSuccessful()).isTrue()
  }

}