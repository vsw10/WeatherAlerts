package com.mobiquity.weatheralerts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.GravityCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.jakewharton.rxbinding2.view.RxView
import com.mobiquity.weatheralerts.adapter.LocationsRecyclerAdapter
import com.mobiquity.weatheralerts.extras.LocationConstants
import com.mobiquity.weatheralerts.extras.StatusTypes
import com.mobiquity.weatheralerts.extras.WeatherConstants
import com.mobiquity.weatheralerts.logs.WALog
import com.mobiquity.weatheralerts.model.weather.CurrentWeather
import com.mobiquity.weatheralerts.model.Location
import com.mobiquity.weatheralerts.model.weather.Main
import com.mobiquity.weatheralerts.observables.RealmResultsObservable
import com.mobiquity.weatheralerts.ui.addlocation.AddLocationFragment
import com.mobiquity.weatheralerts.ui.bookmarkedlocations.BookmarkedFragment
import com.mobiquity.weatheralerts.ui.helpscreen.HelpScreenActivity
import com.mobiquity.weatheralerts.ui.pinlocations.PinLocationFragment
import com.mobiquity.weatheralerts.ui.searchlocation.SearchLocationActivity
import com.mobiquity.weatheralerts.ui.searchlocation.SearchLocationFragent
import com.mobiquity.weatheralerts.utils.launchActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.realm.OrderedRealmCollection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

open class MainActivity() :
    BaseActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapLongClickListener,
    View.OnClickListener {
    companion object {
        const val TAG = "MainActivity"
    }

    private var addLocationFragment: AddLocationFragment? = null
    private var searchLocationFragent: SearchLocationFragent? = null
    private var pinLocationFragment: PinLocationFragment? = null
    private var bookmarkedLocationsFragment: BookmarkedFragment? = null


    private var addLocation: SupportMapFragment? = null
    private var bookmarkedLocation: Fragment? = null
    private var helpScreen: Fragment? = null

    lateinit var googlemap: GoogleMap
    lateinit var bundle:Bundle

    private val locationDisposables: CompositeDisposable = CompositeDisposable()

    val viewModel by lazy {
        ViewModelProviders.of(this).get(MainActivtyViewModel::class.java)
    }

    val log: Logger by lazy { LoggerFactory.getLogger(MainActivity::class.java) }

    init {
        val realmResults = realm.where(Location::class.java).findAllAsync()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WALog.debug(TAG, logger = log, " onCreate")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        bundle = Bundle()

        syncDrawerState()

        addLocation = supportFragmentManager
            .findFragmentById(R.id.mark_location) as? SupportMapFragment
        initUi()
        addLocation?.getMapAsync(this)
        //initApis()


        initObservables()
        initRecyclerAdapter()
    }

    override fun onResume() {
        super.onResume()
        viewModel.startCheckingLocationUpdates()
        /* viewModel?.latLang?.let {
             markLocation(googlemap,it)
         }*/
    }

    fun initUi() {
        val imageViewAdd: AppCompatImageView =
            activityBaseBinding.navView.findViewById(R.id.imageViewAdd)

        val pinLocationText: AppCompatTextView =
            activityBaseBinding.navView.findViewById(R.id.pin_location_text)

        val helpScreenText: AppCompatTextView =
            activityBaseBinding.navView.findViewById(R.id.help_screen)

        activityBaseBinding.extendedFab.setOnClickListener(this)

        imageViewAdd.setOnTouchListener(View.OnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    WALog.debug(TAG, logger = log, "imageViewAdd Action Down Clicked")
                    true
                }
                MotionEvent.ACTION_UP -> {
                    WALog.debug(TAG, logger = log, "imageViewAdd Action Up Clicked")
                    launchActivity(
                        context = applicationContext,
                        clazz = SearchLocationActivity::class.java,
                        flag = Intent.FLAG_ACTIVITY_NEW_TASK
                    )
                    true
                }
                else -> false
            }
        })

        pinLocationText.setOnTouchListener(View.OnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    WALog.debug(TAG, logger = log, "pinLocationText Action Down Clicked")
                    true
                }
                MotionEvent.ACTION_UP -> {
                    WALog.debug(TAG, logger = log, "pinLocationText Action Up Clicked")
                    /*addLocation?.let {
                        removeFragment(it)
                    }*/
                    showAddLocationFragment()

                    true
                }
                else -> false
            }
        })

        helpScreenText.setOnTouchListener(View.OnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    WALog.debug(TAG, logger = log, "helpScreenText Action Down Clicked")
                    true
                }
                MotionEvent.ACTION_UP -> {
                    WALog.debug(TAG, logger = log, "helpScreenText Action Up Clicked")
                    /*addLocation?.let {
                        removeFragment(it)
                    }*/
                    showHelp()
                    true
                }
                else -> false
            }
        })


        val listener = NavigationView.OnNavigationItemSelectedListener { item ->
            Log.d(TAG, "OnNavigationItemClicked ${item.itemId}")
            when (item.itemId) {

                R.id.imageViewAdd -> {
                    launchActivity(
                        context = applicationContext,
                        clazz = SearchLocationActivity::class.java,
                        flag = Intent.FLAG_ACTIVITY_NEW_TASK
                    )
                    true
                }
                R.id.pin_location_text -> {
                    addLocation?.let { removeFragment(it) }
                    showAddLocationFragment()
                    true
                }
                else -> false
            }
        }

        activityBaseBinding.navView.setNavigationItemSelectedListener(listener)

    }

    fun initApis() {
        locationDisposables.add(
            viewModel.errorObservable
                .subscribe { error ->
                    WALog.error("", log, "errorObservable  ${error.message}", error)
                    when (error::class) {
                        retrofit2.HttpException::class, HttpException::class -> {
                            val http = error as HttpException
                            WALog.debug(
                                "TAG", logger = log,
                                "showError: HTTP error code ${http.code()}"
                            )
                            if (http.code() == 401) {
                                WALog.debug(
                                    "TAG", logger = log,
                                    "Action Exit the App"
                                )
                            }
                        }
                    }
                }
        )

        val realmLocations = realm.where(Location::class.java).findAllAsync()
        locationDisposables.add(
            RealmResultsObservable.from(realmLocations)
                .doOnError({
                    WALog.debug(
                        "", logger = log, "Error in RealmResultsObservable" +
                                "  ${it.message}"
                    )
                })
                .subscribe({ _locations ->
                    WALog.debug("", logger = log, "Location Size is ${_locations.size}")
                    viewModel.restartPolling()
                }, {
                    WALog.debug("", logger = log, "Location Size is Error $it")
                }
                )
        )
    }

    fun initObservables() {
        locationDisposables.add(
            viewModel.responseCityObservable
                .subscribe(
                    {
                        var success = it.second.message
                        WALog.debug(
                            TAG, logger = log,
                            "responseCityObservable $success"
                        )
                        when (success) {
                            StatusTypes.SUCCESS.getValue() -> {
                                var currentWeather: CurrentWeather? = it.first?.body()
                                var mainValues: Main? = currentWeather?.main
                                WALog.debug(
                                    TAG, logger = log, "Observable Success " +
                                            " Main Values ${mainValues}"
                                )
                                WALog.debug(
                                    TAG, logger = log, "Observable Success " +
                                            " ${it.first?.body()}"
                                )
                                if (currentWeather != null) {
                                    performCalculations(currentWeather)
                                }
                            }
                            else -> {
                                bookmarkedLocationsFragment?.let { it1 -> removeFragment(it1) }
                                Toast.makeText(applicationContext,
                                    resources.getString(R.string.error_weather_updates_api),
                                    Toast.LENGTH_LONG).show()
                                WALog.debug(TAG, logger = log, "Observable ERROR")
                            }
                        }
                    }
                )
        )

        locationDisposables.add(
            viewModel.responseLocationObservable
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(500, TimeUnit.MILLISECONDS)
                .timeout(5, TimeUnit.SECONDS)
                .subscribe(
                    {
                        var success = it.second.message
                        when (success) {
                            StatusTypes.SUCCESS.getValue() -> {
                                WALog.info(TAG, logger = log, "Notify Data SetChanged")
                                val adapter =
                                    activityBaseBinding.navHeaderMain.locationRecyclerView.adapter as LocationsRecyclerAdapter
                                adapter?.setLocation()
                            }
                            else -> {
                                WALog.info(TAG, logger = log, "No Need to Notify Data SetChanged")
                            }
                        }
                    },
                    {

                    }
                )
        )
    }

    fun syncDrawerState() {
        WALog.debug(TAG, logger = log, "syncDrawerState")
        setSupportActionBar(activityBaseBinding.toolbar)
        activityBaseBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        val toggle = ActionBarDrawerToggle(
            this,
            activityBaseBinding.drawerLayout,
            activityBaseBinding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        activityBaseBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    fun showAddLocationFragment() {
        Log.d("vs", "Show Fragment")
        activityBaseBinding.drawerLayout.closeDrawer(GravityCompat.START, true)
        if (addLocationFragment == null || addLocation?.isVisible != true) {
            addLocation?.let {
                addLocation
                setVisible(true)
                addLocationFragment?.let { addLocationFragment ->
                    AddLocationFragment.newInstance()
                    addFragment(addLocationFragment, addLocation!!)
                }
            }

        }

    }

    fun addFragment(fragmentName: Fragment, fragmentContainerView: Fragment) {
        WALog.info(TAG, logger = log, " Add Fragment")
        fragmentName.let {
            if (supportFragmentManager.fragments.isNotEmpty()) {
                supportFragmentManager.beginTransaction()
                    .replace(fragmentContainerView.getId(), it)
                    .disallowAddToBackStack()
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .add(fragmentContainerView.getId(), it)
                    .disallowAddToBackStack()
                    .commit()
            }
        }
        when (fragmentName) {
            is SupportMapFragment -> {
                googlemap?.addMarker(
                    MarkerOptions()
                        .position(viewModel.latLang)
                )
                googlemap.moveCamera(CameraUpdateFactory.newLatLng(viewModel.latLang))
            }
            else -> {
                googlemap?.clear()
            }
        }
    }

    fun removeFragment(fragmentName: Fragment) {
       WALog.debug(TAG,logger = log,"Fragment Removed ")
        fragmentName.let {
            supportFragmentManager.beginTransaction()
                .remove(it)
                .commit()
        }

        showAddLocationFragment()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googlemap = googleMap
        markLocation(googlemap)
        googleMap.setOnMapLongClickListener(this)
        googleMap.setOnMarkerClickListener(this)

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        marker.title = "Add this location"
        marker.isVisible = true

        return true
    }

    override fun onMapLongClick(latLang: LatLng) {

        val location = latLang
        viewModel.latLang = location
        googlemap.clear()
        viewModel.getLocationAddress(latLng = location)

        /* showYesNoAlertWithoutFocus(applicationContext as BaseActivity, "Selected Position") { _: DialogInterface?, _: Int ->
         }*/
        Toast.makeText(applicationContext, "Please Click To Add", Toast.LENGTH_SHORT)
            .show()
    }

    fun markLocation(map: GoogleMap) {
        locationDisposables.add(
            viewModel.responseGeocodeObservable.subscribe(
                {
                    var success = it.second.message

                    WALog.debug(
                        TAG, logger = log, "markLocation responseGeocodeObservable" +
                                "$success"
                    )
                    when (success) {
                        StatusTypes.SUCCESS.getValue() -> {
                            activityBaseBinding.extendedFab.isVisible = true
                            val markerOptions = MarkerOptions()
                            markerOptions.position(viewModel.latLang)
                            MainActivtyViewModel.addressDetails = it.first?.get(0)!!
                            var name: String? = it.first?.get(0)?.locality
                            WALog.debug(TAG, logger = log, "localityName $name")
                            name?.let {
                            } ?: run {
                                name = it.first?.get(0)?.adminArea
                                if (name.isNullOrEmpty()) {
                                    name = it.first?.get(0)?.subAdminArea
                                }
                            }
                            WALog.debug(TAG, logger = log, "CityName $name")
                            markerOptions.title("Current Selected Location")
                            markerOptions.snippet(name)
                            map.addMarker(
                                markerOptions
                            ).showInfoWindow()
                            map.moveCamera(CameraUpdateFactory.newLatLng(viewModel.latLang))
                        }
                        else -> {

                        }
                    }

                }, {
                    activityBaseBinding.extendedFab.isGone = true
                    map.clear()
                    Toast.makeText(
                        applicationContext,
                        "Error Selecting Location",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            )
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        locationDisposables.dispose()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        activityBaseBinding.extendedFab.isGone = true
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            activityBaseBinding.extendedFab.id -> {
                var latitude: Boolean = false
                MainActivtyViewModel.addressDetails?.let {
                    latitude = it.hasLatitude()
                }
                if (latitude) {
                    Toast.makeText(applicationContext, "Added Location", Toast.LENGTH_SHORT).show()
                    WALog.info(TAG, logger = log, "Add Click Location ")
                    activityBaseBinding.extendedFab.isGone = true
                    runBlocking {
                        launch {
                            WALog.info(TAG, logger = log, "SAVE IN REALM ")
                            viewModel.saveRealmObjects(Location())
                            delay(1000L)
                            MainActivtyViewModel.addressDetails = null
                        }
                    }
                }
            }
            else -> {
                WALog.info(TAG, logger = log, "Dont Add Location ")
            }
        }
    }

    fun initRecyclerAdapter() {
        val locations = realm.where(Location::class.java).findAllAsync()
        val locationsRecyclerAdapter =
            LocationsRecyclerAdapter(this, locations as OrderedRealmCollection<Location>)

        activityBaseBinding.navHeaderMain.locationRecyclerView.layoutManager =
            LinearLayoutManager(this)
        activityBaseBinding.navHeaderMain.locationRecyclerView.adapter = locationsRecyclerAdapter
        activityBaseBinding.navHeaderMain.locationRecyclerView.isNestedScrollingEnabled = true

        with(activityBaseBinding.navHeaderMain.locationRecyclerView.itemAnimator) {
            if (this is SimpleItemAnimator) {
                this.supportsChangeAnimations = true
            }
        }

        locationsRecyclerAdapter.clickSubjectObservable
            ?.takeUntil(RxView.clicks(activityBaseBinding.navHeaderMain.locationRecyclerView))
            ?.subscribe(
                {
                    bookmarkedLocationsFragment?.let { it1 -> removeFragment(it1) }
                    activityBaseBinding.drawerLayout.closeDrawer(GravityCompat.START, true)
                    WALog.info(TAG, logger = log, "clickSubjectObservable")
                    var localityName = it.mLocality
                    WALog.info(TAG, logger = log, "clickSubjectObservable ${localityName}")
                    bundle.putString(LocationConstants.CITY_NAME, it.mLocality)
                    viewModel.getCurrentWeatherByCityName(localityName)
                },
                {
                    WALog.info(TAG, logger = log, "clickSubjectObservable ${it}")
                }
            )
    }

    // Function to display the Help & FAQs
    fun showHelp() {
        launchActivity(
            applicationContext,
            HelpScreenActivity::class.java,
            null,
            Intent.FLAG_ACTIVITY_NEW_TASK
        )
    }

    fun showBookMarkedLocationFragment(bundle: Bundle) {
        WALog.debug(TAG, logger = log, "showAddLocationFragment")
        bookmarkedLocationsFragment = BookmarkedFragment.newInstance(bundle)
        activityBaseBinding.drawerLayout.closeDrawer(GravityCompat.START, true)
        bookmarkedLocationsFragment?.let {
            if (supportFragmentManager.fragments.isNotEmpty()) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, it)
                    .disallowAddToBackStack()
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container_view, it)
                    .disallowAddToBackStack()
                    .commit()
            }
        }
    }


    fun performCalculations(currentWeather: CurrentWeather)
    {
        bundle.putString(WeatherConstants.TEMPERATURE,currentWeather.main?.temp.toString())
        bundle.putString(WeatherConstants.MIN_TEMPERATURE,currentWeather.main?.tempMin.toString())
        bundle.putString(WeatherConstants.MAX_TEMPERATURE,currentWeather.main?.tempMax.toString())
        bundle.putString(WeatherConstants.HUMIDITY,currentWeather.main?.humidity.toString())
        bundle.putString(WeatherConstants.PRESSURE,currentWeather.main?.pressure.toString())
        bundle.putString(WeatherConstants.FEELS_LIKE,currentWeather.main?.feelsLike.toString())
        bundle.putString(WeatherConstants.COUNTRY,currentWeather.sys?.country)
        bundle.putString(WeatherConstants.RAIN, currentWeather.weather?.get(0)?.description.toString())
        bundle.putString(WeatherConstants.VISIBILITY,currentWeather.visibility.toString())
        WALog.debug(TAG,logger = log," performCalculations ${currentWeather.main?.temp.toString()}" +
                " ${currentWeather.main?.tempMin.toString()}")
        showBookMarkedLocationFragment(bundle)
    }
}