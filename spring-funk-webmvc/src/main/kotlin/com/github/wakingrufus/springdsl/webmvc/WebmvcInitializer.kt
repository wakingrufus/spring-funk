package com.github.wakingrufus.springdsl.webmvc

import com.github.wakingrufus.funk.base.getDsl
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.jackson.JacksonProperties
import org.springframework.boot.autoconfigure.jackson.standardJacksonObjectMapperBuilderCustomizer
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.servlet.*
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.boot.ssl.SslBundles
import org.springframework.boot.web.server.WebServerFactoryCustomizerBeanPostProcessor
import org.springframework.boot.web.servlet.WebListenerRegistrar
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean
import org.springframework.web.filter.RequestContextFilter
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import java.util.function.Supplier


class WebmvcInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    private val log = KotlinLogging.logger {}

    override fun initialize(context: GenericApplicationContext) {
        val binder = Binder.get(context.environment)
        val webProperties = binder.bindOrCreate("spring.web", WebProperties::class.java)
        val serverProperties = binder.bindOrCreate("server", ServerProperties::class.java)
        val webMvcProperties = binder.bindOrCreate("spring.mvc", WebMvcProperties::class.java)

        context.getDsl<WebmvcDsl>()?.run {
            enableWebmvcDsl?.run {
                // context.addBeanFactoryPostProcessor(ServletContextAwareProcessor())
                context.registerBean<WebMvcProperties> { webMvcProperties }
                servletWebServerFactoryAutoConfiguration(context, serverProperties)
                dispatcherServletAutoConfiguration(context, webMvcProperties)
                webMvcAutoConfiguration(context, webProperties, webMvcProperties)
                engine?.initialize(serverProperties, context)
            }
            converterDsl?.run {
                converters.forEach {
                    it.getInitializer().invoke(context)
                }
                jacksonProperties?.run {
                    val jacksonProps = Binder.get(context.environment).bind(
                        "spring.jackson",
                        Bindable.ofInstance(this)
                    )
                        .orElseGet { JacksonProperties() }
                    standardJacksonObjectMapperBuilderCustomizer(context, jacksonProps)
                }
            }
            routerDsl?.run {
                context.registerBean<RouterFunction<ServerResponse>> { this }
            }
        }
    }

    private fun servletWebServerFactoryAutoConfiguration(
        context: GenericApplicationContext,
        serverProperties: ServerProperties
    ) {
        context.registerBean(
            "webServerFactoryCustomizerBeanPostProcessor",
            WebServerFactoryCustomizerBeanPostProcessor::class.java,
            Supplier { WebServerFactoryCustomizerBeanPostProcessor() }
        )
        context.registerBean(
            ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar::class.java,
            Supplier { ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar() }
        )
        val servletWebServerFactoryConfiguration = ServletWebServerFactoryAutoConfiguration()
        context.registerBean(ServletWebServerFactoryCustomizer::class.java, Supplier {
            servletWebServerFactoryConfiguration.servletWebServerFactoryCustomizer(
                serverProperties,
                context.getBeanProvider(WebListenerRegistrar::class.java),
                context.getBeanProvider(CookieSameSiteSupplier::class.java),
                context.getBeanProvider(SslBundles::class.java)
            )
        })

        if (serverProperties.forwardHeadersStrategy == ServerProperties.ForwardHeadersStrategy.FRAMEWORK) {
            forwardedHeaderFilter(context)
        }

    }

    private fun dispatcherServletAutoConfiguration(
        context: GenericApplicationContext,
        webMvcProperties: WebMvcProperties
    ) {
        dispatcherServletConfiguration(context, webMvcProperties)
        dispatcherServletRegistrationConfiguration(context, webMvcProperties)

    }

    private fun webMvcAutoConfiguration(
        context: GenericApplicationContext,
        webProperties: WebProperties,
        webMvcProperties: WebMvcProperties
    ) {

        val webMvcConfiguration = WebMvcAutoConfiguration()
        context.registerBean(
            OrderedHiddenHttpMethodFilter::class.java,
            Supplier { webMvcConfiguration.hiddenHttpMethodFilter() })
        webMvcConfigurationAdapter(context, webProperties, webMvcProperties)
        context.registerBean(
            RequestContextFilter::class.java,
            Supplier { WebMvcAutoConfigurationAdapter.requestContextFilter() })
        enableWebMvcConfiguration(context, webMvcProperties, webProperties)
    }
}