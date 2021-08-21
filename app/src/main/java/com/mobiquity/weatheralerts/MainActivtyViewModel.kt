package com.mobiquity.weatheralerts

import android.app.Application
import android.location.Address
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.maps.model.LatLng
import com.mobiquity.weatheralerts.extras.Configuration
import com.mobiquity.weatheralerts.helper.OpenWeatherMapHelper
import com.mobiquity.weatheralerts.helper.PersistenceHelper
import com.mobiquity.weatheralerts.logs.WALog
import com.mobiquity.weatheralerts.model.weather.CurrentWeather
import com.mobiquity.weatheralerts.model.Location
import com.mobiquity.weatheralerts.observables.RealmObjectObservable
import com.mobiquity.weatheralerts.utils.GeocodersData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import io.realm.Realm
import io.realm.RealmObject
import org.slf4j.LoggerFactory
import retrofit2.Response

class MainActivtyViewModel(application: Application):
    AndroidViewModel(application) {
    private val app: WeatherAlert by lazy { getApplication<WeatherAlert>() }

    lateinit var latLang : LatLng

    private val allDisposables = CompositeDisposable()

    val log: org.slf4j.Logger by lazy {
        LoggerFactory.getLogger(MainActivtyViewModel::class.java)
    }

    private val lastLocationUpdateSubject = ReplaySubject.create<String>()
    val lastLocationUpdateObservable: Observable<String> get() = lastLocationUpdateSubject.share()

    private val errorSubject = PublishSubject.create<Throwable>()
    val errorObservable: Observable<Throwable> get() = errorSubject.share()

    private val responseCitySubject = PublishSubject.create<Pair<Response<CurrentWeather?>?,Throwable>>()
    val responseCityObservable: Observable<Pair<Response<CurrentWeather?>?,Throwable>> get() = responseCitySubject.share()

    private val responseGeocodeSubject = PublishSubject.create<Pair<List<Address>?,Throwable>>()
    val responseGeocodeObservable: Observable<Pair<List<Address>?,Throwable>> get() = responseGeocodeSubject.share()

    private val responseLocationSubject = PublishSubject.create<Pair<Location?,Throwable>>()
    val responseLocationObservable: Observable<Pair<List<Address>?,Throwable>> get() = responseGeocodeSubject.share()

    fun restartPolling() {
        WALog.debug("",logger = log,"Restart Polling ")
        allDisposables.add(pollLocations())
    }


    private fun pollLocations(): Disposable {
        val service = WeatherAlert.getApiService(app)
        return Observable.defer {
            service.currentWeather("london", Configuration.WEATHER_API_KEY).toObservable()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                    error -> errorSubject.onNext(error)
            }
            .subscribe( { apilist ->
                {
                    weatherAlertApp?.let {
                        //weatherAlertApp.currentUser()
                    }
                    WALog.debug("", logger = log, "Current User is $apilist")
                }
            },{
                WALog.debug("", logger = log, "Current User is $it")
            }
            )
    }

    fun getCurrentWeatherByCityName(cityName:String?){
        WALog.debug("", logger = log,
            "getCurrentWeatherByCityName $cityName")
        OpenWeatherMapHelper(app.applicationContext,Configuration.WEATHER_API_KEY)
            .getCurrentWeatherByCityName(cityName,responseCitySubject)
    }

    fun getLocationAddress(latLng: LatLng){
        GeocodersData().getLocationAddress(app.applicationContext,
            latLang,responseGeocodeSubject)

    }

    fun saveRealmObjects(realmObject:RealmObject){
        PersistenceHelper.saveResult(realmObject,addressDetails)
    }

    fun startCheckingLocationUpdates(){
        allDisposables.clear()
        allDisposables.add(locationChanges())
    }

    private fun locationChanges(): Disposable {
        WALog.debug("", log, "locationChanges()...called")
        Realm.getDefaultInstance().use { realm ->
            return RealmObjectObservable.from(
                realm
                    .where(Location::class.java)
                    .findFirstAsync()
            ).subscribe(
                {

                    responseLocationSubject.onNext(Pair(it, Throwable(message = "SUCCESS")))
                   /* it.id.let {

                    }*/
                },
                {
                    responseLocationSubject.onNext(Pair(null, Throwable(message = "ERROR")))
                }
            )
        }
    }

    companion object{
        var addressDetails: Address? = null
    }
}