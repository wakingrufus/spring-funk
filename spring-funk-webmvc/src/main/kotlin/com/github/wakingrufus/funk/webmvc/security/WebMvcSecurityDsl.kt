package com.github.wakingrufus.funk.webmvc.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.HttpSecurityDsl
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.context.SecurityContextRepository

class WebMvcSecurityDsl {
    var authenticationManager: AuthenticationManager? = null

    var userDetailsService: UserDetailsService? = null

    var passwordEncoder: PasswordEncoder? = null

    var userDetailsPasswordService: UserDetailsPasswordService? = null

    var securityContextRepository: SecurityContextRepository? = null

    private var httpConfiguration: HttpSecurityDsl.() -> Unit = {}

    fun http(httpConfiguration: HttpSecurityDsl.() -> Unit = {}) {
        this.httpConfiguration = httpConfiguration
    }

}
