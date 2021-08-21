package com.mobiquity.weatheralerts.ui.addlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.mobiquity.weatheralerts.R
import com.mobiquity.weatheralerts.databinding.FragmentLocationBinding

class AddLocationFragment : Fragment(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapLongClickListener{

    private lateinit var addLocationViewModel: AddLocationViewModel
    private var fragmentLocationBinding : FragmentLocationBinding? = null

    lateinit var googlemap: GoogleMap

    private val binding get() = fragmentLocationBinding!!

    private lateinit var linearProgress: LinearProgressIndicator


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addLocationViewModel =
            ViewModelProvider(this).get(AddLocationViewModel::class.java)

        fragmentLocationBinding = FragmentLocationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //linearProgress = view.findViewById<LinearProgressIndicator>(R.id.linear_progress)

        val mapFragment =
            view.findFragment<SupportMapFragment>() as? SupportMapFragment

        mapFragment?.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentLocationBinding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        linearProgress?.let {
            it.isGone = true
        }
        googlemap = googleMap ?: return
        //markLocation(googlemap)
        googleMap.setOnMapLongClickListener(this)
        googleMap.setOnMarkerClickListener(this)


        googleMap.setOnMapLongClickListener(this)
        googleMap.setOnMarkerClickListener(this)


    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Toast.makeText(requireContext(), "Clicked Location ", Toast.LENGTH_SHORT)
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
        // [START_EXCLUDE silent]
        googlemap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    companion object {
        fun newInstance(): AddLocationFragment{
            return AddLocationFragment()
        }
    }

}