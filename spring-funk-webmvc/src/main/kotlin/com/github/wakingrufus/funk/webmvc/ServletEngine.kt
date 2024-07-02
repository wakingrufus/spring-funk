package com.github.wakingrufus.funk.webmvc

import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.context.support.GenericApplicationContext

interface ServletEngine {
    fun initialize(serverProperties: ServerProperties, context: GenericApplicationContext)
}
