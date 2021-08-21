package com.mobiquity.weatheralerts.logs

import android.util.Log
import com.mobiquity.weatheralerts.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object WALog {

    val log by lazy {
        LoggerFactory.getLogger("OkHttp")
    }
    private const val TAG = "wa"

    internal var enableLogger: Boolean = true
        set(value) {
            field = value
            httpLogLevel = fetchLogLevel()
        }
    private var httpLogLevel = fetchLogLevel()
        private set(value) {
            field = value
            httpLoggingInterceptor.level = value
        }

    val httpLoggingInterceptor = HttpLoggingInterceptor(
     {
            if (BuildConfig.DEBUG || enableLogger) {
                info("OkHttp", log, it)
            }
        }
    ).apply {
        level = httpLogLevel
    }


    private fun fetchLogLevel() = when {
        enableLogger -> HttpLoggingInterceptor.Level.BASIC
        BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
        else -> HttpLoggingInterceptor.Level.NONE
    }


    fun debug(tag: String, logger: Logger, message: String, enableLogger: Boolean = this.enableLogger) {
        Log.d(TAG,"$tag $message")
        if (enableLogger) logger.debug(message)
    }

    fun error(tag: String, logger: Logger, message: String, error: Throwable, enableLogger: Boolean = this.enableLogger) {
        Log.e(TAG, "$tag $message")
        if (enableLogger) logger.error(message, error)
    }

    fun info(tag: String, logger: Logger, message: String, enableLogger: Boolean = this.enableLogger) {
        Log.i(TAG, "$tag $message")
        if (enableLogger) logger.info(message)
    }

    fun trace(tag: String, logger: Logger, message: String, enableLogger: Boolean = this.enableLogger) {
        Log.v(TAG, "$tag $message")
        if (enableLogger) logger.trace(message)
    }

    fun warn(tag: String, logger: Logger, message: String, enableLogger: Boolean = this.enableLogger) {
        Log.w(TAG, "$tag $message")
        if (enableLogger) logger.warn(message)
    }

}