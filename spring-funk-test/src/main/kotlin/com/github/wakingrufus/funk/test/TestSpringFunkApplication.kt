package com.github.wakingrufus.funk.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.wakingrufus.funk.base.SpringFunkApplication
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.core.SpringDslMarker
import org.springframework.boot.test.mock.web.SpringBootMockServletContext
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.Ordered
import org.springframework.http.MediaType
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.mock.env.MockEnvironment
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

/**
 * useful for integration testing DSL enabled initializers
 */
class TestSpringFunkApplication(
    private val initializers: List<ApplicationContextInitializer<GenericApplicationContext>>
) {
    private lateinit var application: SpringFunkApplication
    private lateinit var environment: MockEnvironment
    private lateinit var test: (GenericApplicationContext) -> Unit
    private var contextFactory: () -> GenericApplicationContext = { GenericApplicationContext() }
    private var isWeb: Boolean = false

    @SpringDslMarker
    fun application(customConfig: SpringDslContainer.() -> Unit): SpringFunkApplication {
        application = SpringFunkApplication {
            customConfig
        }
        return application
    }

    @SpringDslMarker
    fun mockWebApplication(customConfig: SpringDslContainer.() -> Unit): SpringFunkApplication {
        isWeb = true
        contextFactory = {
            ServletWebServerApplicationContext().apply {
                servletContext = SpringBootMockServletContext("/")
            }
        }
        application = SpringFunkApplication {
            customConfig
        }
        return application
    }

    @SpringDslMarker
    fun webApplication(customConfig: SpringDslContainer.() -> Unit): SpringFunkApplication {
        isWeb = true
        contextFactory = {
            ServletWebServerApplicationContext()
        }
        application = SpringFunkApplication {
            customConfig
        }
        return application
    }

    @SpringDslMarker
    fun environment(config: MockEnvironment.() -> Unit) {
        environment = MockEnvironment().apply(config)
    }

    @SpringDslMarker
    var testRestTemplate: TestRestTemplate? = null

    @SpringDslMarker
    var mockMvc: MockMvc? = null

    @SpringDslMarker
    fun test(test: GenericApplicationContext.() -> Unit) {
        this.test = test
    }

    fun run() {
        val context = contextFactory.invoke()
        context.environment = environment
        (initializers + application)
            .sortedBy {
                when (it) {
                    is Ordered -> it.order
                    else -> 1
                }
            }
            .forEach {
                it.initialize(context)
            }
        context.refresh()
        if (isWeb && context is ServletWebServerApplicationContext) {
            if (context.webServer != null) {
                val jacksonConverter = MappingJackson2HttpMessageConverter(ObjectMapper())
                jacksonConverter.supportedMediaTypes = listOf(
                    MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_OCTET_STREAM
                )
                testRestTemplate = TestRestTemplate(
                    RestTemplateBuilder()
                        .messageConverters(jacksonConverter, StringHttpMessageConverter())
                        .rootUri("http://localhost:" + context.webServer.port)
                )
            } else {
                mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
            }
        }
        try {
            test.invoke(context)
        } finally {
            if (context.isActive) {
                context.close()
            }
        }
    }
}

@SpringDslMarker
fun testDslApplication(
    vararg initializers: ApplicationContextInitializer<GenericApplicationContext>,
    test: TestSpringFunkApplication.() -> Unit
) {
    TestSpringFunkApplication(initializers.toList()).apply(test).run()
}
