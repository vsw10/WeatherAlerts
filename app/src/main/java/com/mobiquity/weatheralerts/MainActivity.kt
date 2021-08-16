package com.mobiquity.weatheralerts

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mobiquity.weatheralerts.ui.addlocation.AddLocationFragment
import com.mobiquity.weatheralerts.ui.bookmarkedlocations.BookmarkedFragment
import com.mobiquity.weatheralerts.ui.helpscreen.HelpScreenFragment
import io.reactivex.disposables.CompositeDisposable

class MainActivity() :
    BaseActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapLongClickListener
{

    private var addLocationFragment: AddLocationFragment? = null
    private var bookmarkedLocationsFragment: BookmarkedFragment? = null
    private var helpScreenFragment:HelpScreenFragment? = null

    private var addLocation : SupportMapFragment? = null
    private var bookmarkedLocation: Fragment? = null
    private var helpScreen: Fragment? = null

    lateinit var googlemap: GoogleMap

    private val locationDisposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        syncDrawerState()
        addLocation = supportFragmentManager
            .findFragmentById(R.id.mark_location) as? SupportMapFragment
        bookmarkedLocation = supportFragmentManager
            .findFragmentById(R.id.display_weather) as? Fragment
        helpScreen = supportFragmentManager
            .findFragmentById(R.id.info_screen) as? Fragment
        //showAddLocationFragment()

        addLocation?.getMapAsync(this)
    }

    fun syncDrawerState(){
        setSupportActionBar(activityBaseBinding.appBarMain.toolbar)
        activityBaseBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        val toggle =ActionBarDrawerToggle(
            this,
            activityBaseBinding.drawerLayout,
            activityBaseBinding.appBarMain.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)

        activityBaseBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    fun showAddLocationFragment(){
        if(addLocationFragment == null || addLocation?.isVisible != true){
            addLocation?.let { addLocation
                setVisible(true)
                addLocationFragment?.let { addLocationFragment ->
                    AddLocationFragment.newInstance()
                    addFragment(addLocationFragment, addLocation!!)
                }
            }

        }

    }

    fun hideLocationFragment() {

    }

    fun showBookmarkedFragment() {

    }

    fun hideBookmarkedFragment(){

    }

    fun showHelpScreenFragment() {

    }

    fun hideHelpScreenFragment() {

    }

    fun addFragment(fragmentName:Fragment, fragmentContainerView: Fragment) {
        fragmentName?.let {
            if(supportFragmentManager.fragments.isNotEmpty()){
                supportFragmentManager.beginTransaction()
                    .add(fragmentContainerView.getId(),it)
                    .disallowAddToBackStack()
                    .commit()
            }
        }
    }

    fun removeFragment(fragmentName:Fragment) {
        fragmentName?.let {
            supportFragmentManager.beginTransaction()
                .remove(it)
                .commit()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googlemap = googleMap ?: return

        googleMap.setOnMapLongClickListener(this)
        googleMap.setOnMarkerClickListener(this)

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Toast.makeText(applicationContext, "Clicked Location ", Toast.LENGTH_SHORT)
            .show()
        return true
    }

    override fun onMapLongClick(latLang: LatLng) {

        val location = latLang
        googlemap.clear()

        googlemap.addMarker(
            MarkerOptions()
                .position(location)
        )
        googlemap.moveCamera(CameraUpdateFactory.newLatLng(location))
        Toast.makeText(applicationContext, "Long Clicked Location ${latLang}", Toast.LENGTH_SHORT)
            .show()
    }
}