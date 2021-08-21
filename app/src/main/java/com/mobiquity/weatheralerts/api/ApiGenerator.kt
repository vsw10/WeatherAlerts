package com.mobiquity.weatheralerts.api

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.mobiquity.weatheralerts.BuildConfig
import com.mobiquity.weatheralerts.WeatherAlert
import com.mobiquity.weatheralerts.logs.WALog
import io.realm.RealmObject
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.reflect.Type
import java.security.KeyStore
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.net.ssl.*
import javax.security.cert.CertificateException

object ApiGenerator {

    private val gson = GsonBuilder()
        .setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                return false
            }

            override fun shouldSkipField(f: FieldAttributes?): Boolean {
                return f?.declaringClass?.equals(RealmObject::class.java) == true
            }
        }).create()

    var retrofit: Retrofit? = null
    val postsInProgress = AtomicInteger(0)

    private fun getBaseUrl(context: Context): String {
        return "https://api.openweathermap.org"
    }

    private fun getBuilder(context: Context): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl(context))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
    }

    fun <S> createService(ctx: Context, serviceClass: Class<S>): S {
        retrofit = getBuilder(ctx)
            .build()
        return retrofit!!.create(serviceClass)
    }

    private val customConverterFactory: Converter.Factory = object : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
        ): JSONConverter {
            return JSONConverter()
        }

        override fun requestBodyConverter(
            type: Type,
            parameterAnnotations: Array<Annotation>,
            methodAnnotations: Array<Annotation>,
            retrofit: Retrofit
        ): Converter<*, RequestBody?>? {
            return BodyConverter()
        }
    }

    /**
     * Class used for JSONObject Converter for response
     */
    private class JSONConverter : Converter<ResponseBody, JSONObject?> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): JSONObject? {
            return try {
                JSONObject(value.string())
            } catch (e: JSONException) {
                throw IOException("Failed to parse JSON", e)
            }
        }
    }

    /**
     * Class used for Body converter for request body
     */
    private class BodyConverter : Converter<JSONObject, RequestBody?> {
        @Throws(IOException::class)
        override fun convert(value: JSONObject): RequestBody? {
            return RequestBody.create(MediaType.parse("application/json"), value.toString())
        }
    }

}