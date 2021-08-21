package com.mobiquity.weatheralerts.model.weather

import com.google.gson.annotations.SerializedName

class Snow(@SerializedName("1h")
           val oneHour: Double? = null,
           @SerializedName("3h")
           val threeHour: Double? = null)