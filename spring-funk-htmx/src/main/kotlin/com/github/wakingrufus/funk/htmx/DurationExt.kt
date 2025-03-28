package com.github.wakingrufus.funk.htmx

import java.time.Duration

val OneSecond = Duration.ofSeconds(1)
fun Duration.toHtmx(): String{
    return if(this < OneSecond){
        "${this.toMillis()}ms"
    } else {
        "${this.seconds}s"
    }
}