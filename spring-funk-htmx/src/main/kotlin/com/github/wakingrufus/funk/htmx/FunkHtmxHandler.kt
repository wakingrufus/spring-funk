package com.github.wakingrufus.funk.htmx

import org.springframework.beans.factory.BeanFactory

fun interface FunkHtmxHandler {
    fun callBeanHandler(bf: BeanFactory, request: Record): Record
}

internal fun <CONTROLLER : Any, REQ : Record> beanAwareHandler(
    controllerClass: Class<CONTROLLER>,
    function: CONTROLLER.(REQ) -> Record
): FunkHtmxHandler {
    return FunkHtmxHandler { bf, request -> function.invoke(bf.getBean(controllerClass), request as REQ) }
}