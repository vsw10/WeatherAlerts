package com.mobiquity.weatheralerts.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Location(var label: String? = null) : RealmObject()
    {
        @PrimaryKey
        var id: String? = null
        var country: String? = null
        var zip: String? = null
        var state: String? = null
        var city: String? = null
        var street: String? = null
        var latitude: Double? = null
        var longitude: Double? = null
}