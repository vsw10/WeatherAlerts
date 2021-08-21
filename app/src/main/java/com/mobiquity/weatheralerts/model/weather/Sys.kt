package com.mobiquity.weatheralerts.model.weather

import com.google.gson.annotations.SerializedName


data class Sys (
    @SerializedName("type")
    val type: Double = 0.0,

    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("message")
    val message: Double? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("sunrise")
    val sunrise: Long? = null,

    @SerializedName("sunset")
    val sunset: Long? = null,

    @SerializedName("pod")
    val pod: Char? = null,
)