package com.mobiquity.weatheralerts.helper

import android.content.Context
import androidx.annotation.NonNull
import com.mobiquity.weatheralerts.MainActivtyViewModel
import com.mobiquity.weatheralerts.WeatherAlert
import com.mobiquity.weatheralerts.api.APIEndPoints
import com.mobiquity.weatheralerts.logs.WALog
import com.mobiquity.weatheralerts.model.weather.CurrentWeather
import com.mobiquity.weatheralerts.model.ThreeHourForecast
import io.reactivex.subjects.PublishSubject

import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection


class OpenWeatherMapHelper(context: Context,apiKey: String?) {
    private lateinit var apiEndPoints : APIEndPoints
    private val options: MutableMap<String, String>

    val log: org.slf4j.Logger by lazy {
        LoggerFactory.getLogger(MainActivtyViewModel::class.java)
    }

    fun getCurrentWeatherByCityName(city: String?,
                                    publishSubject: PublishSubject<Pair<Response<CurrentWeather?>?,Throwable>> ) {
        options[QUERY] = city!!
        apiEndPoints.getCurrentWeatherByCityName(options)
            ?.enqueue(object : Callback<CurrentWeather?> {
                override fun onResponse(
                    call: Call<CurrentWeather?>,
                    response: Response<CurrentWeather?>
                ) {
                    WALog.debug(TAG, logger = log,
                        "getCurrentWeatherByCityName onResponse ${response.body().toString()}")

                    handleCurrentWeatherResponse(response,publishSubject)
                }

                override fun onFailure(
                    @NonNull call: Call<CurrentWeather?>?,
                    @NonNull throwable: Throwable?
                ) {
                    WALog.debug("", logger = log,
                        "getCurrentWeatherByCityName onFailure ")
                    publishSubject.onNext(Pair(null, Throwable(message =
                    "Error")))
                }
            })
    }

    private fun handleCurrentWeatherResponse(
        response: Response<CurrentWeather?>,
        publishSubject: PublishSubject<Pair<Response<CurrentWeather?>?, Throwable>>
    ) {

        val responseCodeValue = response.body().toString()
        if(responseCodeValue.isNullOrEmpty()){
            publishSubject.onNext(Pair(response, Throwable(message =
            "UnAuthorized")))
            return
        }
        //WALog.debug(TAG,logger = log," handleCurrentWeatherResponse CurrentWeather ${currentWeather}")
        if (response.code() === HttpURLConnection.HTTP_OK) {

            publishSubject.onNext(Pair(response, Throwable(message = "SUCCESS")))
        } else if (response.code() === HttpURLConnection.HTTP_FORBIDDEN || response.code() === HttpURLConnection.HTTP_UNAUTHORIZED) {
            publishSubject.onNext(Pair(response, Throwable(message =
            "UnAuthorized")))
        } else {
            try {
                publishSubject.onNext(Pair(response, Throwable(message =
                "UnAuthorized")))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun handleThreeHourForecastResponse(
        response: Response<ThreeHourForecast?>
    ) {
        if (response.code() === HttpURLConnection.HTTP_OK) {
        } else if (response.code() === HttpURLConnection.HTTP_FORBIDDEN ||
            response.code() === HttpURLConnection.HTTP_UNAUTHORIZED) {
        } else {
            try {

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val TAG = "OpenWeatherMapHelper"
        private const val APPID = "appId"
        private const val UNITS = "units"
        private const val LANGUAGE = "lang"
        private const val QUERY = "q"
        private const val CITY_ID = "id"
        private const val LATITUDE = "lat"
        private const val LONGITUDE = "lon"
        private const val ZIP_CODE = "zip"
    }

    init {
        apiEndPoints =  WeatherAlert.getApiService(context = context )

        options = HashMap()
        options[APPID] = apiKey ?: ""
    }
}