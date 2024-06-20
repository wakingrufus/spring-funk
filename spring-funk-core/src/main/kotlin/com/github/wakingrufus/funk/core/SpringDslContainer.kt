package com.github.wakingrufus.funk.core

import java.util.HashMap
import java.util.function.Consumer

class SpringDslContainer {
    private val map: MutableMap<Class<out SpringDsl>, SpringDsl> = HashMap<Class<out SpringDsl>, SpringDsl>()

    /**
     * register a [SpringDsl] to the container
     */
    @SpringDslMarker
    fun <T : SpringDsl> register(dsl: T): T {
        map.put(dsl::class.java, dsl)
        return dsl
    }

    @SpringDslMarker
    inline fun <reified T : SpringDsl> configure(crossinline config: T.() -> Unit) {
        configure(T::class.java) { it.config() }
    }
    /**
     * A convenience method to instantiate a dsl and configure it in a single statement.
     */
    @SpringDslMarker
    fun <T : SpringDsl> configure(clazz: Class<T>, dsl: Consumer<T>): T {
        val ctor = clazz.getConstructor()
        ctor.setAccessible(true)
        val instance: T = ctor.newInstance()
        dsl.accept(instance)
        return register(instance)
    }


    @SpringDslMarker
    fun <T : SpringDsl> configure(clazz: Class<T>): T {
        val ctor = clazz.getConstructor()
        ctor.setAccessible(true)
        val instance: T = ctor.newInstance()
        return register(instance)
    }


    @SpringDslMarker
    fun <T : SpringDsl> create(clazz: Class<T>): T {
        val ctor = clazz.getConstructor()
        ctor.setAccessible(true)
        val instance: T = ctor.newInstance()
        return instance
    }


    fun <T : SpringDsl> get(dslClass: Class<T>): T? {
        return map[dslClass] as T?
    }
}