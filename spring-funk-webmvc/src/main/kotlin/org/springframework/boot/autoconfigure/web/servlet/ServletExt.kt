package org.springframework.boot.autoconfigure.web.servlet

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.MultipartConfigElement
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration.DispatcherServletConfiguration
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration.DispatcherServletRegistrationConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean
import org.springframework.core.ResolvableType
import org.springframework.format.support.FormattingConversionService
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.util.PathMatcher
import org.springframework.validation.Validator
import org.springframework.web.accept.ContentNegotiationManager
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.ForwardedHeaderFilter
import org.springframework.web.servlet.*
import org.springframework.web.servlet.function.support.HandlerFunctionAdapter
import org.springframework.web.servlet.function.support.RouterFunctionMapping
import org.springframework.web.servlet.handler.HandlerMappingIntrospector
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
import org.springframework.web.servlet.resource.ResourceUrlProvider
import org.springframework.web.servlet.view.BeanNameViewResolver
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver
import org.springframework.web.servlet.view.InternalResourceViewResolver
import org.springframework.web.util.UrlPathHelper
import java.util.function.Supplier

private val log = KotlinLogging.logger {}

fun forwardedHeaderFilter(context: GenericApplicationContext) {
    context.registerBean<FilterRegistrationBean<ForwardedHeaderFilter>> {
        ServletWebServerFactoryAutoConfiguration.ForwardedHeaderFilterConfiguration().forwardedHeaderFilter(
            context.getBeanProvider(ServletWebServerFactoryAutoConfiguration.ForwardedHeaderFilterCustomizer::class.java)
        )
    }
}

fun webMvcConfigurationAdapter(
    context: GenericApplicationContext,
    webProperties: WebProperties,
    webMvcProperties: WebMvcProperties
) {

    val webMvcConfigurationAdapter: Supplier<WebMvcAutoConfigurationAdapter> =
        object : Supplier<WebMvcAutoConfigurationAdapter> {
            private val configuration: WebMvcAutoConfigurationAdapter by lazy {
                val configuration = WebMvcAutoConfigurationAdapter(
                    webProperties,
                    webMvcProperties,
                    context,
                    context.getBeanProvider(
                        HttpMessageConverters::class.java
                    ),
                    context.getBeanProvider(WebMvcAutoConfiguration.ResourceHandlerRegistrationCustomizer::class.java),
                    context.getBeanProvider(
                        DispatcherServletPath::class.java
                    ),
                    context.getBeanProvider(
                        ResolvableType.forClass(
                            ServletRegistrationBean::class.java
                        )
                    )
                )
                configuration
            }

            override fun get(): WebMvcAutoConfigurationAdapter {
                return configuration
            }
        }
    context.registerBean(InternalResourceViewResolver::class.java, Supplier {
        webMvcConfigurationAdapter.get().defaultViewResolver()
    })
    context.registerBean(BeanNameViewResolver::class.java, Supplier {
        webMvcConfigurationAdapter.get().beanNameViewResolver()
    })
    context.registerBean(
        "viewResolver",
        ContentNegotiatingViewResolver::class.java, Supplier { webMvcConfigurationAdapter.get().viewResolver(context) })
}

fun dispatcherServletConfiguration(
    context: GenericApplicationContext,
    webMvcProperties: WebMvcProperties
) {
    val dispatcherServletConfiguration = DispatcherServletConfiguration()
    context.registerBean(
        DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME,
        DispatcherServlet::class.java,
        Supplier { dispatcherServletConfiguration.dispatcherServlet(webMvcProperties) })

}

fun dispatcherServletRegistrationConfiguration(
    context: GenericApplicationContext, webMvcProperties: WebMvcProperties
) {
    try {
        Class.forName("jakarta.servlet.ServletRegistration")
        context.registerBean(
            DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME,
            DispatcherServletRegistrationBean::class.java, Supplier {
                DispatcherServletRegistrationConfiguration().dispatcherServletRegistration(
                    context.getBean(
                        DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME,
                        DispatcherServlet::class.java
                    ), webMvcProperties, context.getBeanProvider(MultipartConfigElement::class.java)
                )
            })
    } catch (ex: ClassNotFoundException) {
        log.debug {
            "Jakarta ServletRegistration not found on classpath. Skipping DispatcherServletRegistrationBean registration"
        }
    }

}

fun enableWebMvcConfiguration(
    context: GenericApplicationContext, webMvcProperties: WebMvcProperties, webProperties: WebProperties
) {
    val enableWebMvcConfiguration: Supplier<EnableWebMvcConfiguration> =
        object : Supplier<EnableWebMvcConfiguration> {
            private val configuration: EnableWebMvcConfiguration by lazy {
                val configuration = EnableWebMvcConfigurationWrapper(
                    webMvcProperties,
                    webProperties,
                    context.getBeanProvider(WebMvcRegistrations::class.java),
                    context.getBeanProvider(WebMvcAutoConfiguration.ResourceHandlerRegistrationCustomizer::class.java),
                    context
                )
                configuration.applicationContext = context
                configuration.servletContext = (context as WebApplicationContext).servletContext
                configuration.setResourceLoader(context)
                configuration
            }

            override fun get(): EnableWebMvcConfiguration {
                return configuration
            }
        }

    context.registerBean(FormattingConversionService::class.java, Supplier {
        enableWebMvcConfiguration.get().mvcConversionService()
    })

    context.registerBean(Validator::class.java, Supplier { enableWebMvcConfiguration.get().mvcValidator() })
    context.registerBean(ContentNegotiationManager::class.java, Supplier {
        enableWebMvcConfiguration.get().mvcContentNegotiationManager()
    })
    context.registerBean(
        WebMvcAutoConfiguration.ResourceChainResourceHandlerRegistrationCustomizer::class.java,
        Supplier {
            WebMvcAutoConfiguration.ResourceChainCustomizerConfiguration().resourceHandlerRegistrationCustomizer(
                webProperties
            )
        })
    context.registerBean(PathMatcher::class.java, Supplier { enableWebMvcConfiguration.get().mvcPathMatcher() })
    context.registerBean(UrlPathHelper::class.java, Supplier { enableWebMvcConfiguration.get().mvcUrlPathHelper() })
    context.registerBean(HandlerMapping::class.java, Supplier {
        enableWebMvcConfiguration.get().viewControllerHandlerMapping(
            context.getBean(
                FormattingConversionService::class.java
            ), context.getBean(ResourceUrlProvider::class.java)
        )
    })
    context.registerBean(RouterFunctionMapping::class.java, Supplier {
        enableWebMvcConfiguration.get().routerFunctionMapping(
            context.getBean(
                FormattingConversionService::class.java
            ), context.getBean(ResourceUrlProvider::class.java)
        )
    })
    context.registerBean(HandlerMapping::class.java, Supplier {
        enableWebMvcConfiguration.get().resourceHandlerMapping(
            context.getBean(
                ContentNegotiationManager::class.java
            ), context.getBean(FormattingConversionService::class.java), context.getBean(
                ResourceUrlProvider::class.java
            )
        )
    })
    context.registerBean(ResourceUrlProvider::class.java, Supplier {
        enableWebMvcConfiguration.get().mvcResourceUrlProvider()
    })
    context.registerBean(HandlerMapping::class.java, Supplier {
        enableWebMvcConfiguration.get().defaultServletHandlerMapping()
    })
    context.registerBean(HandlerFunctionAdapter::class.java, Supplier {
        enableWebMvcConfiguration.get().handlerFunctionAdapter()
    })
    context.registerBean(HttpRequestHandlerAdapter::class.java, Supplier {
        enableWebMvcConfiguration.get().httpRequestHandlerAdapter()
    })
    context.registerBean(SimpleControllerHandlerAdapter::class.java, Supplier {
        enableWebMvcConfiguration.get().simpleControllerHandlerAdapter()
    })
    context.registerBean(HandlerExceptionResolver::class.java, Supplier {
        enableWebMvcConfiguration.get().handlerExceptionResolver(
            context.getBean(
                ContentNegotiationManager::class.java
            )
        )
    })
    context.registerBean(ViewResolver::class.java, Supplier {
        enableWebMvcConfiguration.get().mvcViewResolver(
            context.getBean(
                ContentNegotiationManager::class.java
            )
        )
    })
    context.registerBean<HandlerMappingIntrospector>(
        "mvcHandlerMappingIntrospector",
        { bd: BeanDefinition -> bd.isLazyInit = true }) {
        enableWebMvcConfiguration.get().mvcHandlerMappingIntrospector()
    }
    context.registerBean(WelcomePageHandlerMapping::class.java, Supplier {
        enableWebMvcConfiguration.get().welcomePageHandlerMapping(
            context, context.getBean(
                FormattingConversionService::class.java
            ), context.getBean(ResourceUrlProvider::class.java)
        )
    })
    context.registerBean(
        DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME,
        LocaleResolver::class.java, Supplier { enableWebMvcConfiguration.get().localeResolver() })
    context.registerBean(
        DispatcherServlet.FLASH_MAP_MANAGER_BEAN_NAME,
        FlashMapManager::class.java, Supplier { enableWebMvcConfiguration.get().flashMapManager() })
    context.registerBean(
        DispatcherServlet.REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME,
        RequestToViewNameTranslator::class.java, Supplier { enableWebMvcConfiguration.get().viewNameTranslator() })
}

private class EnableWebMvcConfigurationWrapper(
    webMvcProperties: WebMvcProperties,
    webProperties: WebProperties,
    mvcRegistrationsProvider: ObjectProvider<WebMvcRegistrations?>?,
    resourceHandlerRegistrationCustomizerProvider: ObjectProvider<WebMvcAutoConfiguration.ResourceHandlerRegistrationCustomizer?>?,
    beanFactory: ListableBeanFactory?
) : EnableWebMvcConfiguration(
    webMvcProperties,
    webProperties,
    mvcRegistrationsProvider,
    resourceHandlerRegistrationCustomizerProvider,
    beanFactory
) {
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        applicationContext?.getBeanProvider(HttpMessageConverter::class.java)?.orderedStream()
            ?.forEach { e: HttpMessageConverter<*>? ->
                converters.add(
                    e
                )
            }
    }

    override fun configureHandlerExceptionResolvers(exceptionResolvers: MutableList<HandlerExceptionResolver>) {
        exceptionResolvers.add(DefaultHandlerExceptionResolver())
    }
}
