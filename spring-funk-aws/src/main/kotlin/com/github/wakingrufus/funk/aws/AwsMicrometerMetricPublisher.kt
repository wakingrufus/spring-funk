package com.github.wakingrufus.funk.aws

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.core.instrument.MeterRegistry
import software.amazon.awssdk.metrics.MetricCollection
import software.amazon.awssdk.metrics.MetricPublisher

import java.time.Duration


class AwsMicrometerMetricPublisher(private val name: String, private val meterRegistry: MeterRegistry) :
    MetricPublisher {
    private val log = KotlinLogging.logger {}
    override fun publish(metricCollection: MetricCollection) {
        val serviceId = metricCollection.first { it.metric().name().equals("ServiceId") }.value().toString()

        val operationName = metricCollection.first { it.metric().name().equals("OperationName") }.value().toString()

        val duration = (metricCollection.first { it.metric().name().equals("ApiCallDuration") }.value() as Duration?)
            ?: Duration.ZERO

        val retries = (metricCollection.first { it.metric().name().equals("RetryCount") }.value() as Int?) ?: 0

        val success =
            (metricCollection.first { it.metric().name().equals("ApiCallSuccessful") }.value() as Boolean?) ?: false

        log.debug {
            "serviceId=$serviceId operationName=$operationName duration=${duration.toMillis()} retries=$retries success=$success"
        }
        val successToken = if (success) "Success" else "Failure"

        if (retries > 0) {
            meterRegistry.counter(
                "awssdk.retries",
                "service", serviceId,
                "name", name,
                "outcome", successToken,
                "operation", operationName
            ).increment(retries.toDouble())
        }
        meterRegistry.timer(
            "awssdk",
            "service", serviceId,
            "name", name,
            "outcome", successToken,
            "operation", operationName
        ).record(duration)
    }

    override fun close() {

    }
}
