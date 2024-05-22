package org.springframework.boot.autoconfigure.jackson

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.JacksonObjectMapperBuilderConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.JacksonObjectMapperConfiguration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

fun standardJacksonObjectMapperBuilderCustomizer(
    context: GenericApplicationContext,
    jacksonProperties: JacksonProperties
) {
    context.registerBean<Jackson2ObjectMapperBuilderCustomizer> {
        JacksonAutoConfiguration.Jackson2ObjectMapperBuilderCustomizerConfiguration()
            .standardJacksonObjectMapperBuilderCustomizer(
                jacksonProperties,
                context.getBeanProvider(Module::class.java)
            )
    }
    val configuration = JacksonObjectMapperBuilderConfiguration()
    context.registerBean<Jackson2ObjectMapperBuilder> {
        configuration.jacksonObjectMapperBuilder(
            context,
            context.getBeansOfType(
                Jackson2ObjectMapperBuilderCustomizer::class.java
            ).values.toList()
        )
    }
    context.registerBean<ObjectMapper> {
        JacksonObjectMapperConfiguration().jacksonObjectMapper(
            context.getBean(Jackson2ObjectMapperBuilder::class.java)
        )
    }
}
