package com.github.wakingrufus.funk.webmvc.security


import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.BeanDefinitionCustomizer
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.ResolvableType
import org.springframework.security.access.expression.SecurityExpressionHandler
import org.springframework.security.config.annotation.ObjectPostProcessor
import org.springframework.security.config.annotation.SecurityConfigurer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer
import java.util.function.Consumer
import java.util.function.Supplier
import jakarta.servlet.Filter
import org.springframework.security.config.annotation.web.configuration.HttpSecurityInitializer

/**
 * [ApplicationContextInitializer] adapter for [WebSecurityConfiguration].
 */
class WebSecurityInitializer(private val httpSecurityDsl: Consumer<HttpSecurity>?) :
    ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) {
        val configurationSupplier: Supplier<WebSecurityConfiguration> = object : Supplier<WebSecurityConfiguration> {
            private var configuration: WebSecurityConfiguration? = null

            override fun get(): WebSecurityConfiguration {
                if (configuration == null) {
                    configuration = WebSecurityConfiguration()
                    val httpSecurity = context.getBean(
                        HttpSecurityInitializer.HTTPSECURITY_BEAN_NAME,
                        HttpSecurity::class.java
                    )

                    httpSecurityDsl?.accept(httpSecurity)

                    try {
                        configuration!!.setFilterChains(
                            listOf<SecurityFilterChain>(httpSecurity.build())
                        )
                    } catch (e: Exception) {
                        throw IllegalStateException(e)
                    }

                    val webSecurityConfigurers: MutableList<SecurityConfigurer<Filter, WebSecurity>> =
                        ArrayList<SecurityConfigurer<Filter, WebSecurity>>()
                    val webSecurityConfigurerBeanNames = context.getBeanNamesForType(
                        ResolvableType.forClassWithGenerics(
                            SecurityConfigurer::class.java, Filter::class.java,
                            WebSecurity::class.java
                        )
                    )
                    for (webSecurityConfigurerBeanName in webSecurityConfigurerBeanNames) {
                        webSecurityConfigurers.add(
                            context.getBean(webSecurityConfigurerBeanName) as SecurityConfigurer<Filter, WebSecurity>
                        )
                    }
                    try {
                        configuration!!.setFilterChainProxySecurityConfigurer(
                            context.getBean(
                                ObjectPostProcessor::class.java
                            ), webSecurityConfigurers
                        )
                    } catch (e: Exception) {
                        throw IllegalStateException(e)
                    }
                }
                return configuration
            }
        }

        context.registerBean(
            SecurityExpressionHandler::class.java,
            Supplier { configurationSupplier.get().webSecurityExpressionHandler() },
            BeanDefinitionCustomizer { bd: BeanDefinition ->
                bd.setDependsOn(
                    AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME
                )
            }
        )
        context.registerBean<T>(
            AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME,
            Filter::class.java,
            Supplier<T?> {
                try {
                    return@registerBean configurationSupplier.get().springSecurityFilterChain()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                null
            }
        )
        context.registerBean(
            WebInvocationPrivilegeEvaluator::class.java,
            Supplier { configurationSupplier.get().privilegeEvaluator() },
            BeanDefinitionCustomizer { bd: BeanDefinition ->
                bd.setDependsOn(
                    AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME
                )
            }
        )
    }
}