package com.mobiquity.weatheralerts.extras

enum class StatusTypes(private val string:String) {
    SUCCESS("SUCCESS"),
    ERROR("ERROR"),
    UNAUTHORIZED("UnAuthorized")
    ;

    fun getValue():String{
        return string
    }
}