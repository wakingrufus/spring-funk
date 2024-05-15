package org.springframework.boot.autoconfigure.jackson

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.JacksonObjectMapperBuilderConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.JacksonObjectMapperConfiguration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.util.function.Supplier


fun standardJacksonObjectMapperBuilderCustomizer(
    context: GenericApplicationContext,
    jacksonProperties: JacksonProperties,
    modules: ObjectProvider<Module>
) {
    context.registerBean(
        Jackson2ObjectMapperBuilderCustomizer::class.java,
        Supplier {
            JacksonAutoConfiguration.Jackson2ObjectMapperBuilderCustomizerConfiguration()
                .standardJacksonObjectMapperBuilderCustomizer(jacksonProperties, modules)
        }
    )
    val configuration = JacksonObjectMapperBuilderConfiguration()
    context.registerBean(Jackson2ObjectMapperBuilder::class.java, {
        configuration.jacksonObjectMapperBuilder(
            context,
            context.getBeansOfType(
                Jackson2ObjectMapperBuilderCustomizer::class.java
            ).values.toList()
        )
    })
    context.registerBean(ObjectMapper::class.java, {
        JacksonObjectMapperConfiguration().jacksonObjectMapper(
            context.getBean(Jackson2ObjectMapperBuilder::class.java)
        )
    })
}
