package com.mobiquity.weatheralerts

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.mobiquity.weatheralerts.logs.WALog
import com.mobiquity.weatheralerts.model.Location
import com.mobiquity.weatheralerts.ui.addlocation.AddLocationFragment
import com.mobiquity.weatheralerts.ui.bookmarkedlocations.BookmarkedFragment
import com.mobiquity.weatheralerts.ui.helpscreen.HelpScreenActivity
import com.mobiquity.weatheralerts.ui.pinlocations.PinLocationFragment
import com.mobiquity.weatheralerts.ui.searchlocation.SearchLocationActivity
import com.mobiquity.weatheralerts.ui.searchlocation.SearchLocationFragent
import com.mobiquity.weatheralerts.utils.launchActivity
import io.reactivex.disposables.CompositeDisposable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MainBackupActivity : BaseActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private var addLocationFragment: AddLocationFragment? = null
    private var searchLocationFragent: SearchLocationFragent? = null
    private var pinLocationFragment: PinLocationFragment? = null
    private var bookmarkedLocationsFragment: BookmarkedFragment? = null


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
        //setContentView(R.layout.main_backup_activity)
        WALog.debug(TAG,logger = log," onCreate ")
        syncDrawerState()

        initUi()
        //showAddLocationFragment()

    }

    fun syncDrawerState(){
        WALog.debug(TAG,logger = log,"syncDrawerState")
        setSupportActionBar(activityBaseBinding.toolbar)
        activityBaseBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        val toggle = ActionBarDrawerToggle(
            this,
            activityBaseBinding.drawerLayout,
            activityBaseBinding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)

        activityBaseBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    fun initUi(){
        val imageViewAdd: AppCompatImageView =
            activityBaseBinding.navView.findViewById(R.id.imageViewAdd)

        val pinLocationText: AppCompatTextView =
            activityBaseBinding.navView.findViewById(R.id.pin_location_text)

        val helpScreenText: AppCompatTextView =
            activityBaseBinding.navView.findViewById(R.id.help_screen)

        //activityBaseBinding.extendedFab.setOnClickListener(this)
        setOnClickListeners(imageViewAdd,
            pinLocationText,
            helpScreenText)
    }

    fun setOnClickListeners(imageViewAdd:AppCompatImageView,
    pinLocationText:AppCompatTextView,
    helpScreenText:AppCompatTextView) {

        imageViewAdd.setOnTouchListener(View.OnTouchListener {
                _, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    WALog.debug(TAG,logger = log,"imageViewAdd Action Down Clicked")
                    true
                }
                MotionEvent.ACTION_UP -> {
                    WALog.debug(TAG,logger = log,"imageViewAdd Action Up Clicked")
                    launchActivity(context = applicationContext,
                        clazz = SearchLocationActivity::class.java,
                        flag = Intent.FLAG_ACTIVITY_NEW_TASK)
                    true
                }
                else -> false
            }
        })

        pinLocationText.setOnTouchListener(View.OnTouchListener {
                _, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    WALog.debug(TAG,logger = log,"pinLocationText Action Down Clicked")
                    true
                }
                MotionEvent.ACTION_UP -> {
                    WALog.debug(TAG,logger = log,"pinLocationText Action Up Clicked")
                    //showAddLocationFragment()
                    true
                }
                else -> false
            }
        })

        helpScreenText.setOnTouchListener(View.OnTouchListener {
                _, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    WALog.debug(TAG,logger = log,"helpScreenText Action Down Clicked")
                    true
                }
                MotionEvent.ACTION_UP -> {
                    WALog.debug(TAG,logger = log,"helpScreenText Action Up Clicked")
                    showHelp()
                    true
                }
                else -> false
            }
        })
    }

    // Function to show the Add Location/PIN Location Fragment
    /*fun showAddLocationFragment(){
        WALog.debug(TAG,logger = log,"showAddLocationFragment")
        addLocationFragment = AddLocationFragment.newInstance()
        activityBaseBinding.drawerLayout.closeDrawer(GravityCompat.START, true)
        addLocationFragment?.let {
            if(supportFragmentManager.fragments.isNotEmpty()){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view,it)
                    .disallowAddToBackStack()
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container_view,it)
                    .disallowAddToBackStack()
                    .commit()
            }
        }
    }*/

    // Function to display the Help & FAQs
    fun showHelp(){
        launchActivity(applicationContext,
            HelpScreenActivity::class.java,
            null,
            Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}