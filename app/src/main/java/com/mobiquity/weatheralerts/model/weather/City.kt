package com.mobiquity.weatheralerts.model.weather

import com.google.gson.annotations.SerializedName

data class City (
    @SerializedName("id")
    val id: Long = 0,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("coord")
    val coord: Coord? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("timezone")
    val timezone: Long? = null,

    @SerializedName("population")
    val population: Long? = null,

    @SerializedName("sunrise")
    val sunrise: Long? = null,

    @SerializedName("sunset")
    val sunset: Long? = null
)