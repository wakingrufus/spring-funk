package org.springframework.security.config.annotation.web.configuration

import org.springframework.beans.factory.getBean
import org.springframework.context.support.GenericApplicationContext
import org.springframework.security.authentication.AuthenticationEventPublisher
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher
import org.springframework.security.config.annotation.ObjectPostProcessor
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder


internal fun authenticationManagerBuilder(context: GenericApplicationContext): AuthenticationManagerBuilder {
    val defaultPasswordEncoder = HttpSecurityConfiguration.LazyPasswordEncoder(context)
    val authenticationEventPublisher = getAuthenticationEventPublisher(context)
    val result = HttpSecurityConfiguration.DefaultPasswordEncoderAuthenticationManagerBuilder(
        context.getBean(), defaultPasswordEncoder
    )

    if (authenticationEventPublisher != null) {
        result.authenticationEventPublisher(authenticationEventPublisher)
    }
    return result
}

internal fun getAuthenticationEventPublisher(context: GenericApplicationContext): AuthenticationEventPublisher {
    if (context.getBeanNamesForType(AuthenticationEventPublisher::class.java).isNotEmpty()) {
        return context.getBean(AuthenticationEventPublisher::class.java)
    }
    return context.getBean<ObjectPostProcessor<DefaultAuthenticationEventPublisher>>()
        .postProcess(DefaultAuthenticationEventPublisher())
}

internal fun lazyPasswordEncoder(context: GenericApplicationContext): PasswordEncoder {
    return HttpSecurityConfiguration.LazyPasswordEncoder(context)
}

internal fun defaultPasswordEncoderAuthenticationManagerBuilder(
    objectPostProcessor: ObjectPostProcessor<Any>,
    passwordEncoder: PasswordEncoder
): AuthenticationManagerBuilder {
    return HttpSecurityConfiguration.DefaultPasswordEncoderAuthenticationManagerBuilder(
        objectPostProcessor, passwordEncoder
    )
}