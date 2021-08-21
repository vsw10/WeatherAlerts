package com.mobiquity.weatheralerts

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.mobiquity.weatheralerts.databinding.ActivityBaseBinding
import com.mobiquity.weatheralerts.databinding.ActivityMainBinding
import io.realm.Realm

abstract class BaseActivity : AppCompatActivity() {

    val realm: Realm by lazy {

        Realm.getDefaultInstance()
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var activityBaseBinding: ActivityBaseBinding

    lateinit var navHeaderMain: AppCompatImageButton

    lateinit var toolbar:Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityBaseBinding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(activityBaseBinding.root)
        setSupportActionBar(activityBaseBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        activityBaseBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        supportPostponeEnterTransition()
        activityBaseBinding.extendedFab.isGone = true
        val decor = window.decorView
        decor.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                decor.viewTreeObserver.removeOnPreDrawListener(this)
                supportStartPostponedEnterTransition()
                return true
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}