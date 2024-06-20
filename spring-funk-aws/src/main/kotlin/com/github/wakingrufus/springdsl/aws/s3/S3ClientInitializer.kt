package com.github.wakingrufus.springdsl.aws.s3

import com.github.wakingrufus.springdsl.aws.AwsDsl
import com.github.wakingrufus.springdsl.aws.AwsMicrometerMetricPublisher
import com.github.wakingrufus.springdsl.aws.GlobalAwsConfigurationProperties
import com.github.wakingrufus.springdsl.base.getDsl
import com.github.wakingrufus.funk.util.normalizeConfigKey
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.getBeanProvider
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder
import java.net.URI

class S3ClientInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        applicationContext.getDsl<AwsDsl>()?.run {
            val binder = Binder.get(applicationContext.environment)
            val globalConfig =
                binder.bind(GlobalAwsConfigurationProperties.PREFIX, GlobalAwsConfigurationProperties::class.java)
                    .orElseGet { GlobalAwsConfigurationProperties() }
            if (globalConfig.enabled) {
                s3Dsl.clientMap.forEach { (name, dslConfig) ->
                    val clientName = normalizeConfigKey(name)

                    applicationContext.registerBean<S3AsyncClient>("s3-$clientName") {
                        val effectiveConfig =
                            binder.bind("awssdk.s3.$clientName", Bindable.ofInstance(dslConfig.config))
                                .orElseGet { S3ClientConfigurationProperties() }
                        val customization: S3AsyncClientBuilder.() -> Unit = {
                            dslConfig.customization.invoke(this)
                            effectiveConfig.endpointOverride?.also {
                                endpointOverride(URI.create(it))
                            }
                            globalConfig.s3EndpointOverride?.also {
                                endpointOverride(URI.create(it))
                            }
                            overrideConfiguration { overrideConfig ->
                                dslConfig.overrideConfig.invoke(overrideConfig)
                                if (checkForMeterRegistryClass()) {
                                    val meterRegistry = applicationContext.getBeanProvider<MeterRegistry>()
                                    meterRegistry.ifAvailable {
                                        overrideConfig.addMetricPublisher(AwsMicrometerMetricPublisher(clientName, it))
                                    }
                                }
                            }
                        }
                        newClientBuilder(effectiveConfig).apply(customization).build()
                    }
                }
            }
        }
    }

    private fun checkForMeterRegistryClass(): Boolean {
        return try {
            Class.forName("io.micrometer.core.instrument.MeterRegistry", false, this::class.java.classLoader)
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }
}
