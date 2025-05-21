package org.springframework.security.config.annotation.authentication.configuration

import org.springframework.context.support.GenericApplicationContext

internal fun initializeUserDetailsBeanManagerConfigurer(context: GenericApplicationContext):
        GlobalAuthenticationConfigurerAdapter {
    return InitializeUserDetailsBeanManagerConfigurer(context)
}

internal fun initializeAuthenticationProviderBeanManagerConfigurer(context: GenericApplicationContext): GlobalAuthenticationConfigurerAdapter {
    return InitializeAuthenticationProviderBeanManagerConfigurer(context)
}