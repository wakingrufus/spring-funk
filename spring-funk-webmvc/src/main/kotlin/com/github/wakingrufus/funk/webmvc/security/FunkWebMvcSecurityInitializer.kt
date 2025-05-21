package com.github.wakingrufus.funk.webmvc.security

import com.github.wakingrufus.funk.base.getDsl
import com.github.wakingrufus.funk.webmvc.WebmvcDsl
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.getBean
import org.springframework.beans.factory.getBeanProvider
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean
import org.springframework.security.authentication.AuthenticationEventPublisher
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.ObjectPostProcessor
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter
import org.springframework.security.config.annotation.authentication.configuration.initializeAuthenticationProviderBeanManagerConfigurer
import org.springframework.security.config.annotation.authentication.configuration.initializeUserDetailsBeanManagerConfigurer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.HttpSecurityInitializer
import org.springframework.security.config.annotation.web.configuration.authenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.defaultPasswordEncoderAuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.lazyPasswordEncoder
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter
import org.springframework.web.accept.ContentNegotiationStrategy
import org.springframework.web.accept.HeaderContentNegotiationStrategy

private const val BEAN_NAME_PREFIX =
    "org.springframework.security.config.annotation.web.configuration.HttpSecurityConfiguration."
const val HTTPSECURITY_BEAN_NAME: String = BEAN_NAME_PREFIX + "httpSecurity"


class FunkWebMvcSecurityInitializer: ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) {
        context.getDsl<WebmvcDsl>()?.securityDsl?.run {
            ObjectPostProcessorInitializer().initialize(context)

            // Replace AuthenticationConfiguration
            context.registerBean<AuthenticationManagerBuilder>{
                authenticationManagerBuilder(context)
            }
            context.registerBean<GlobalAuthenticationConfigurerAdapter> {
                AuthenticationConfiguration.enableGlobalAuthenticationAutowiredConfigurer(context)
            }
            context.registerBean<GlobalAuthenticationConfigurerAdapter>("initializeUserDetailsBeanManagerConfigurer") {
                initializeUserDetailsBeanManagerConfigurer(context)
            }
            context.registerBean<GlobalAuthenticationConfigurerAdapter>("initializeAuthenticationProviderBeanManagerConfigurer"){
                initializeAuthenticationProviderBeanManagerConfigurer(context)
            }

            val securityInitializer = HttpSecurityInitializer(authenticationManager, userDetailsService, passwordEncoder,
                userDetailsPasswordService)

            securityInitializer.initialize(context)
            context.registerBean(
                HTTPSECURITY_BEAN_NAME,
                HttpSecurity::class.java,
                { httpSecurity(context) },
                { bd: BeanDefinition ->
                    bd.scope = "prototype"
                    bd.isAutowireCandidate = true
                }
            )
            WebSecurityInitializer {
                if (securityContextRepository != null) {
                    it.securityContext().securityContextRepository(securityContextRepository).and()
                } else {
                    it
                }
                    .invoke(httpConfiguration)
            }
                .initialize(context)

            WebMvcSecurityInitializer().initialize(context)
    }
}
    fun httpSecurity(context: GenericApplicationContext): HttpSecurity{
        val passwordEncoder = lazyPasswordEncoder(context)
        val authenticationBuilder: AuthenticationManagerBuilder =
           defaultPasswordEncoderAuthenticationManagerBuilder(context.getBean(), passwordEncoder)
        authenticationBuilder.parentAuthenticationManager(context.getBean<AuthenticationConfiguration>().authenticationManager)
        authenticationBuilder.authenticationEventPublisher(getAuthenticationEventPublisher(context))
        val http = HttpSecurity(context.getBean(), authenticationBuilder, createSharedObjects(context))
        val webAsyncManagerIntegrationFilter = WebAsyncManagerIntegrationFilter()
        webAsyncManagerIntegrationFilter.setSecurityContextHolderStrategy(this.securityContextHolderStrategy)

        // @formatter:off
        http
            .csrf(Customizer.withDefaults())
            .addFilter(webAsyncManagerIntegrationFilter)
            .exceptionHandling(Customizer.withDefaults())
            .headers(Customizer.withDefaults())
            .sessionManagement(Customizer.withDefaults())
            .securityContext(Customizer.withDefaults())
            .requestCache(Customizer.withDefaults())
            .anonymous(Customizer.withDefaults())
            .servletApi(Customizer.withDefaults())
            .apply(DefaultLoginPageConfigurer())
        http.logout(Customizer.withDefaults())

        // @formatter:on
        applyCorsIfAvailable(http)
        applyDefaultConfigurers(http)
        return http
    }
    private fun getAuthenticationEventPublisher(context: GenericApplicationContext): AuthenticationEventPublisher {
        if (context.getBeanNamesForType(AuthenticationEventPublisher::class.java).isNotEmpty()) {
            return context.getBean<AuthenticationEventPublisher>()
        }
        return context.getBean<ObjectPostProcessor<DefaultAuthenticationEventPublisher>>().postProcess(
            DefaultAuthenticationEventPublisher()
        )
    }
    private fun createSharedObjects(context: GenericApplicationContext): Map<Class<*>, Any> {
        val sharedObjects: MutableMap<Class<*>, Any> = HashMap()
        sharedObjects[ApplicationContext::class.java] = context
        sharedObjects[ContentNegotiationStrategy::class.java] = context.getBeanProvider<ContentNegotiationStrategy>().ifAvailable ?: HeaderContentNegotiationStrategy()
        return sharedObjects
    }
}