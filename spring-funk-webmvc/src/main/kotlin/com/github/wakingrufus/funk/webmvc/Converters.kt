package com.github.wakingrufus.funk.webmvc

import org.springframework.beans.factory.BeanRegistrarDsl
import org.springframework.http.converter.ResourceHttpMessageConverter
import org.springframework.http.converter.ResourceRegionHttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter
import tools.jackson.databind.json.JsonMapper

class JACKSON(val config: JsonMapper.Builder.() -> Unit = {}) : Converter {
    override fun register(): BeanRegistrarDsl.() -> Unit = {
        registerBean("mappingJackson2HttpMessageConverter") {
            JacksonJsonHttpMessageConverter(bean())
        }
        registerBean {
            JsonMapper.builder().apply(config).build()
        }
    }
}

object STRING : Converter {
    override fun register(): BeanRegistrarDsl.() -> Unit = {
        registerBean("StringHttpMessageConverter") { StringHttpMessageConverter() }
    }
}

object ATOM : Converter {
    override fun register(): BeanRegistrarDsl.() -> Unit = {
        registerBean("atomFeedHttpMessageConverter") { AtomFeedHttpMessageConverter() }
    }
}

object FORM : Converter {
    override fun register(): BeanRegistrarDsl.() -> Unit = {
        registerBean("allEncompassingFormHttpMessageConverter") { AllEncompassingFormHttpMessageConverter() }
    }
}

object KOTLIN : Converter {
    override fun register(): BeanRegistrarDsl.() -> Unit = {
        registerBean("kotlinSerializationJsonHttpMessageConverter") {
            KotlinSerializationJsonHttpMessageConverter()
        }
    }
}

object RESOURCE : Converter {
    override fun register(): BeanRegistrarDsl.() -> Unit = {
        registerBean("resourceHttpMessageConverter") { ResourceHttpMessageConverter() }
        registerBean("resourceRegionHttpMessageConverter") { ResourceRegionHttpMessageConverter() }
    }
}

object RSS : Converter {
    override fun register(): BeanRegistrarDsl.() -> Unit = {
        registerBean(
            "rssChannelHttpMessageConverter"
        ) { RssChannelHttpMessageConverter() }
    }
}
