package com.github.wakingrufus.springdsl.webmvc

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.support.GenericApplicationContext
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.ResourceHttpMessageConverter
import org.springframework.http.converter.ResourceRegionHttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter
import java.util.function.Supplier


object JACKSON : Converter {
    override fun getInitializer(): GenericApplicationContext.() -> Unit = {
        registerBean("mappingJackson2HttpMessageConverter",
            HttpMessageConverter::class.java,
            Supplier { MappingJackson2HttpMessageConverter(getBean(ObjectMapper::class.java)) })
    }
}

object STRING : Converter {
    override fun getInitializer(): GenericApplicationContext.() -> Unit = {
        registerBean("StringHttpMessageConverter",
            HttpMessageConverter::class.java,
            Supplier { StringHttpMessageConverter() })
    }
}

object ATOM : Converter {
    override fun getInitializer(): GenericApplicationContext.() -> Unit = {
        registerBean(
            "atomFeedHttpMessageConverter",
            HttpMessageConverter::class.java,
            Supplier { AtomFeedHttpMessageConverter() }
        )
    }
}

object FORM : Converter {
    override fun getInitializer(): GenericApplicationContext.() -> Unit = {
        registerBean(
            "allEncompassingFormHttpMessageConverter",
            HttpMessageConverter::class.java,
            Supplier { AllEncompassingFormHttpMessageConverter() }
        )
    }
}

object KOTLIN : Converter {
    override fun getInitializer(): GenericApplicationContext.() -> Unit = {
        registerBean("kotlinSerializationJsonHttpMessageConverter",
            HttpMessageConverter::class.java,
            Supplier { KotlinSerializationJsonHttpMessageConverter() })
    }
}

object RESOURCE : Converter {
    override fun getInitializer(): GenericApplicationContext.() -> Unit = {
        registerBean("resourceHttpMessageConverter",
            HttpMessageConverter::class.java,
            Supplier { ResourceHttpMessageConverter() })
        registerBean(
            "resourceRegionHttpMessageConverter",
            HttpMessageConverter::class.java,
            Supplier { ResourceRegionHttpMessageConverter() }
        )
    }
}

object RSS : Converter {
    override fun getInitializer(): GenericApplicationContext.() -> Unit = {
        registerBean(
            "rssChannelHttpMessageConverter",
            HttpMessageConverter::class.java,
            Supplier { RssChannelHttpMessageConverter() }
        )
    }
}
