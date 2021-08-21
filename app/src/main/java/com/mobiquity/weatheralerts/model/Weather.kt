package com.mobiquity.weatheralerts.model

import com.google.gson.annotations.SerializedName


data class Weather(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("main")
    val main: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("icon")
    val icon: String? = null
)