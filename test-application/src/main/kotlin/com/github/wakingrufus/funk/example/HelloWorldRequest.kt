package com.github.wakingrufus.funk.example

import java.util.UUID

@JvmRecord
data class HelloWorldRequest(val name: String? = null)

@JvmRecord
data class ThingByIdRequest(val id: UUID)