package com.mobiquity.weatheralerts

import android.app.Application
import android.content.Context
import android.util.Log
import com.mobiquity.weatheralerts.api.APIEndPoints
import com.mobiquity.weatheralerts.api.ApiGenerator
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.mongodb.App

lateinit var weatherAlertApp: App


const val PARTITION_EXTRA_KEY = "PARTITION"
const val PROJECT_NAME_EXTRA_KEY = "PROJECT NAME"

class WeatherAlert: Application() {

    lateinit var weatherAlert: WeatherAlert
    private var apiEndPoints: APIEndPoints? = null

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        /*weatherAlertApp = App(
        AppConfiguration.Builder(Configuration.MONGODB_REALM_APP_ID)
            .defaultSyncErrorHandler{session, error ->
                Log.e("vs","Error message $error")
            }
            .build()
        )*/
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build()
        )

     //Log.d("vs","Initialising REAlM Project ${weatherAlertApp.configuration.appId}")
        Log.d("vs","Initialising REAlM Project ")
    }

    companion object {

        fun getApiService(context: Context): APIEndPoints {
            val application =context.applicationContext as WeatherAlert
            if(application.apiEndPoints == null){
                application.apiEndPoints =
                    ApiGenerator.createService(application,APIEndPoints::class.java)
            }
            return application.apiEndPoints as APIEndPoints
        }
    }


}