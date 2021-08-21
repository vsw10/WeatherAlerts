package com.mobiquity.weatheralerts.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Location() : RealmObject() {
        @PrimaryKey
        var id: Int? = null

        var mCountryName: String? = null
        var mPostalCode: String? = null
        var mCountryCode:String? = null
        var mLocality: String? = null
        var mLatitude: Double? = null
        var mLongitude: Double? = null
        var isDelete: Int? = null
}