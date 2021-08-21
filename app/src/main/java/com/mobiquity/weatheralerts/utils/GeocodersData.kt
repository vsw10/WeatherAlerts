package com.mobiquity.weatheralerts.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import com.mobiquity.weatheralerts.logs.WALog
import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory

class GeocodersData() {

    companion object{
        const val TAG = "GeocodersData"

    }
    val log by lazy {
        LoggerFactory.getLogger(GeocodersData::class.java)
    }

    fun getLocationAddress(context: Context,
                           latLang:LatLng,
                            subject: PublishSubject<Pair<List<Address>?,Throwable>>){

        var geocoder = Geocoder(context)
        val listAddress = geocoder.getFromLocation(latLang.latitude,latLang.longitude,1)
        WALog.debug(
            TAG,logger = log,"getLocationAddress Size IS " +
                "${listAddress.size}")
        listAddress?.let{
            subject.onNext(Pair(listAddress, Throwable(message = "SUCCESS")))
        }
        if(listAddress.isNullOrEmpty()){
            subject.onNext(Pair(null, Throwable(message = "ERROR")))
        }
    }

}