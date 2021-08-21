package com.mobiquity.weatheralerts.model

import com.google.gson.annotations.SerializedName
import com.mobiquity.weatheralerts.model.weather.*


data class ThreeHourForecastWeather (
    @SerializedName("dt")
    var dt: Long? = null,

    @SerializedName("main")
    var main: Main? = null,

    @SerializedName("weather")
    var weather: List<Weather>? = null,

    @SerializedName("clouds")
    var clouds: Clouds? = null,

    @SerializedName("wind")
    var wind: Wind? = null,

    @SerializedName("visibility")
    val visibility: Long? = null,

    @SerializedName("pop")
    val pop: Double? = null,

    @SerializedName("rain")
    var rain: Rain? = null,
    @SerializedName("snow")
    var snow: Snow? = null,

    @SerializedName("sys")
    private var mSys: Sys? = null,

    @SerializedName("dt_txt")
    var dtTxt: String? = null){

    fun getmSys(): Sys? {
        return mSys
    }

    fun setmSys(mSys: Sys?) {
        this.mSys = mSys
    }
}