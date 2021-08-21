package com.mobiquity.weatheralerts.model.weather

import com.google.gson.annotations.SerializedName


data class Clouds (
    @SerializedName("all")
    val all: Double = 0.0
)
