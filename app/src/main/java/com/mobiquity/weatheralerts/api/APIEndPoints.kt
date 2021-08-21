package com.mobiquity.weatheralerts.api

import com.mobiquity.weatheralerts.model.Location
import io.reactivex.Single
import retrofit2.http.*
import retrofit2.http.QueryMap

import com.mobiquity.weatheralerts.model.weather.CurrentWeather
import retrofit2.Call

import retrofit2.http.GET

interface APIEndPoints {

    //toDo
    @GET(value = ApiEndPointsConstants.CURRENT_WEATHER)
    fun currentWeather(
        @Query("city") city: String,
        @Query("apiKey") apiKey: String,
    ):Single<Location>

    // ToDo
    @GET(value = ApiEndPointsConstants.CURRENT_WEATHER_LATLONG)
    fun currentWeatherByLatLang(
        @Query("lat") lat: String,
        @Query("lon") lang: String,
        @Query("apiKey") apiKey: String,
        cityname: String
    ):Single<Location>

    // ToDo
    @GET(value = ApiEndPointsConstants.HOURLY_FORECAST_LATLONG)
    fun hourlyForecastByLatLang(
        @Query("lat") lat: String,
        @Query("long") lang: String,
        @Query("apiKey") apiKey: String,
        cityname: String
    ):Single<Location>


    @GET(ApiEndPointsConstants.CURRENT)
    fun getCurrentWeatherByCityName(@QueryMap options: MutableMap<String, String>): Call<CurrentWeather?>?

    @GET(ApiEndPointsConstants.CURRENT)
    fun getCurrentWeatherByCityID(@QueryMap options: MutableMap<String, String>): Call<CurrentWeather?>?

    @GET(ApiEndPointsConstants.CURRENT)
    fun getCurrentWeatherByGeoCoordinates(@QueryMap options: MutableMap<String, String>): Call<CurrentWeather?>?
}