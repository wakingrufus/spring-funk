package org.springframework.security.config.annotation.web.configuration

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.security.authentication.AuthenticationEventPublisher
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration.enableGlobalAuthenticationAutowiredConfigurer
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.function.Supplier


/**
 * [ApplicationContextInitializer] adapter for [HttpSecurityConfiguration].
 */
class HttpSecurityInitializer(
    private val authenticationConfiguration: AuthenticationConfiguration?,
    private val authenticationManager: AuthenticationManager?,
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder?,
    private val userDetailsPasswordService: UserDetailsPasswordService
) : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) {
        val configuration = HttpSecurityConfiguration()
        configuration.setApplicationContext(context)

        if (authenticationConfiguration != null) {
            configuration.setAuthenticationConfiguration(authenticationConfiguration)
        } else {
            // build authenticationManager, otherwise HttpSecurityConfiguration will throw NPE
            val authProvider = DaoAuthenticationProvider()
            authProvider.setUserDetailsService(userDetailsService)
            authProvider.setUserDetailsPasswordService(userDetailsPasswordService)

            if (passwordEncoder != null) {
                authProvider.setPasswordEncoder(passwordEncoder)
            } else {
                authProvider.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder())
            }

            val authConfig = AuthenticationConfiguration()
            authConfig.authenticationManager = ProviderManager(authProvider)
            configuration.setAuthenticationConfiguration(authConfig)
        }

        val httpSecuritySupplier =
            Supplier<HttpSecurity> {
                configuration.setObjectPostProcessor(context.getBean())
                try {
                    return@Supplier configuration.httpSecurity()
                } catch (e: Exception) {
                    throw IllegalStateException(e)
                }
            }


    }

    companion object {

    }
}

