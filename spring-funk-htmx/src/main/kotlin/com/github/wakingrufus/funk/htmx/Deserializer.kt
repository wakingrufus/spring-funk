package com.github.wakingrufus.funk.htmx

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.servlet.function.ServerRequest

inline fun <reified REQ : Record> ObjectMapper.deserializeForm(request: ServerRequest): REQ {
    return convertValue(request.params().toSingleValueMap(), REQ::class.java)
}

inline fun <reified REQ : Record> deserializeJson(request: ServerRequest): REQ {
    return request.body(REQ::class.java)
}