package com.github.wakingrufus.springdsl.test

import com.github.wakingrufus.springdsl.base.SpringDslApplication
import com.github.wakingrufus.springdsl.core.SpringDslContainer
import com.github.wakingrufus.springdsl.core.SpringDslMarker
import org.springframework.boot.test.mock.web.SpringBootMockServletContext
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.Ordered
import org.springframework.mock.env.MockEnvironment
import org.springframework.web.context.support.GenericWebApplicationContext

/**
 * useful for integration testing DSL enabled initializers
 */
class TestSpringDslApplication(
    private val initializers: List<ApplicationContextInitializer<GenericApplicationContext>>
) {
    private lateinit var application: SpringDslApplication
    private lateinit var environment: MockEnvironment
    private lateinit var test: (GenericApplicationContext) -> Unit
    private var contextFactory: () -> GenericApplicationContext = { GenericApplicationContext() }

    @SpringDslMarker
    fun application(customConfig: SpringDslContainer.() -> Unit): SpringDslApplication {
        application = SpringDslApplication {
            customConfig
        }
        return application
    }

    @SpringDslMarker
    fun webApplication(customConfig: SpringDslContainer.() -> Unit): SpringDslApplication {
        contextFactory = {
            GenericWebApplicationContext().apply {
                servletContext = SpringBootMockServletContext("/")
            }
        }
        application = SpringDslApplication {
            customConfig
        }
        return application
    }

    @SpringDslMarker
    fun environment(config: MockEnvironment.() -> Unit) {
        environment = MockEnvironment().apply(config)
    }

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
    test: TestSpringDslApplication.() -> Unit
) {
    TestSpringDslApplication(initializers.toList()).apply(test).run()
}
