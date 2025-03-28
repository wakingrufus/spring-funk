package com.github.wakingrufus.funk.htmx.trigger

import com.github.wakingrufus.funk.core.SpringDslMarker
import java.time.Duration


@SpringDslMarker
class HxTriggerDsl {
    private val events: MutableSet<HxTriggerEvent> = mutableSetOf()

    @SpringDslMarker
    fun event(name: String, dsl: StandardEventDsl.() -> Unit = {}) {
        events.add(StandardEventDsl(name).apply(dsl)())
    }

    @SpringDslMarker
    fun poll(duration: Duration){
        events.add(HxTriggerEvent.Poll(duration))
    }

    @SpringDslMarker
    fun load(){
        events.add(HxTriggerEvent.Load)
    }

    @SpringDslMarker
    fun revealed(){
        events.add(HxTriggerEvent.Revealed)
    }

    @SpringDslMarker
    fun intersect(dsl: IntersectDsl.() -> Unit) {
        events.add(IntersectDsl().apply(dsl)())
    }

    operator fun invoke(): HxTrigger{
        return HxTrigger(events)
    }
}