package com.mobiquity.weatheralerts.helper

import android.location.Address
import android.util.Log
import com.mobiquity.weatheralerts.MainActivity
import com.mobiquity.weatheralerts.logs.WALog
import com.mobiquity.weatheralerts.model.Location
import io.realm.Realm
import io.realm.RealmObject
import io.realm.kotlin.createObject
import org.slf4j.LoggerFactory
import java.util.*

object PersistenceHelper {

    val log by lazy {
        LoggerFactory.getLogger(PersistenceHelper::class.java)
    }
    private val pendingObjects: MutableList<RealmObject> = mutableListOf()

    fun saveResult(obj: RealmObject,addressDetails:Address?,
                   onSuccess: Realm.Transaction.OnSuccess? = null, onError: Realm.Transaction.OnError? = null) {
        synchronized(pendingObjects) {
            pendingObjects.add(obj)
        }
        process(addressDetails,onSuccess, onError)
    }

    private fun process(addressDetails: Address?,
                        onSuccess: Realm.Transaction.OnSuccess? = null, onError: Realm.Transaction.OnError? = null) {
                Realm.getDefaultInstance().use { realm ->
                    realm.executeTransactionAsync(
                        object : Realm.Transaction {
                            override fun execute(realm: Realm) {
                                WALog.info("PersistenceHelper",logger = log," " +
                                        " EXECUTE REALM TRANSACTIONS ")
                                var nextId: Int? =
                                    (realm.where(Location::class.java).max("id")?.toInt())
                                if(nextId == null)
                                {
                                    nextId = 1
                                } else {
                                    nextId =
                                    (realm.where(Location::class.java).max("id")?.toInt()?.plus(1))
                                }

                                WALog.info("PersistenceHelper",logger = log," PK Created ${nextId} ")
                                val location = realm.createObject<Location>(nextId)

                                WALog.info("PersistenceHelper",logger = log," Location ID $nextId ")
                                location.mCountryCode = addressDetails?.countryCode
                                var name = addressDetails?.locality

                                name?.let {
                                } ?: run {
                                    name = addressDetails?.adminArea
                                    if (name.isNullOrEmpty()) {
                                        name = addressDetails?.subAdminArea
                                    }
                                }
                                location.mLocality = name
                                location.mCountryName = addressDetails?.countryName
                                location.mPostalCode = addressDetails?.postalCode
                                location.mLatitude = addressDetails?.latitude
                                location.mLongitude = addressDetails?.longitude
                                location.isDelete = 0
                                WALog.info("PersistenceHelper",logger = log," Country Code ${addressDetails?.countryCode} " +
                                        " Locality ${ name }   ${addressDetails?.countryName}   " +
                                        "    ${addressDetails?.latitude}    ${addressDetails?.longitude}   ${addressDetails?.postalCode}")
                                realm.copyToRealmOrUpdate(location)

                                /*  synchronized(pendingObjects) {
                                            val modifiedLocations = mutableSetOf<String>()
                                            pendingObjects.forEach {
                                                when (it.javaClass) {
                                                    Location::class.java -> {
                                                        var mLocality = (it as Location).mLocality
                                                        mLocality = addressDetails?.locality
                                                        WALog.info(MainActivity.TAG,logger = log,"REALM  $mLocality")
                                                        mLocality?.apply {
                                                            modifiedLocations.add(this)
                                                        }
                                                    }
                                                }
                                                realm.copyToRealmOrUpdate(it)
                                            }

                                            modifiedLocations.forEach {
                                                val ll = realm.where(Location::class.java)
                                                    .equalTo("id", it)
                                                    .findFirst() ?: return@forEach
                                                realm.copyToRealmOrUpdate(ll)
                                            }
                                            WALog.info("PersistenceHelper",logger = log," process ")
                                            pendingObjects.clear()
                                        }*/
                            }
                        },
                        onSuccess, onError
                    )
                }

    }
}