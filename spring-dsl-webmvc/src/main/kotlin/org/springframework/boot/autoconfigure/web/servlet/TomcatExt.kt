package org.springframework.boot.autoconfigure.web.servlet

import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.context.support.GenericApplicationContext
import java.util.function.Supplier

fun registerTomcatHeaderForwarder(context: GenericApplicationContext, serverProperties: ServerProperties) {
    context.registerBean(
        ServletWebServerFactoryAutoConfiguration.ForwardedHeaderFilterCustomizer::class.java,
        Supplier {
            ServletWebServerFactoryAutoConfiguration.ForwardedHeaderFilterConfiguration()
                .tomcatForwardedHeaderFilterCustomizer(serverProperties)
        }
    )
}