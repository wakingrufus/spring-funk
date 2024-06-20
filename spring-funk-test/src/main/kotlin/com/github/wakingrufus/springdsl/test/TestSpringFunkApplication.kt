package com.github.wakingrufus.springdsl.test

import com.github.wakingrufus.funk.base.SpringFunkApplication
import com.github.wakingrufus.funk.core.SpringDslContainer
import com.github.wakingrufus.funk.core.SpringDslMarker
import org.springframework.boot.test.mock.web.SpringBootMockServletContext
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.Ordered
import org.springframework.mock.env.MockEnvironment
import org.springframework.web.context.support.GenericWebApplicationContext

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

    @SpringDslMarker
    fun application(customConfig: SpringDslContainer.() -> Unit): SpringFunkApplication {
        application = SpringFunkApplication {
            customConfig
        }
        return application
    }

    @SpringDslMarker
    fun webApplication(customConfig: SpringDslContainer.() -> Unit): SpringFunkApplication {
        contextFactory = {
            GenericWebApplicationContext().apply {
                servletContext = SpringBootMockServletContext("/")
            }
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
    test: TestSpringFunkApplication.() -> Unit
) {
    TestSpringFunkApplication(initializers.toList()).apply(test).run()
}
