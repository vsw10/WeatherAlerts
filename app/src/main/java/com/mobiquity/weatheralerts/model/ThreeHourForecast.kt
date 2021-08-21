package com.mobiquity.weatheralerts.model

import com.google.gson.annotations.SerializedName
import com.mobiquity.weatheralerts.model.weather.City


data class ThreeHourForecast (
    @SerializedName("cod")
    val cod: String? = null,

    @SerializedName("message")
    val message: Double = 0.0,

    @SerializedName("cnt")
    val cnt: Int = 0,

    @SerializedName("list")
    val list: List<ThreeHourForecastWeather>? = null,

    @SerializedName("city")
    val city: City? = null
)