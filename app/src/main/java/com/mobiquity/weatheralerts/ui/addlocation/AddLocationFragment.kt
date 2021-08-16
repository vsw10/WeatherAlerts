package com.mobiquity.weatheralerts.ui.addlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mobiquity.weatheralerts.R
import com.mobiquity.weatheralerts.databinding.FragmentLocationBinding

class AddLocationFragment : Fragment(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapLongClickListener{

    private lateinit var addLocationViewModel: AddLocationViewModel
    private var _binding: FragmentLocationBinding? = null

    lateinit var googlemap: GoogleMap

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addLocationViewModel =
            ViewModelProvider(this).get(AddLocationViewModel::class.java)

        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        /*val textView: TextView = binding.textGallery
        addLocationViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = view.findViewById<View>(R.id.pin_location) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googlemap = googleMap ?: return

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
        Toast.makeText(requireContext(), "Long Clicked Location ${latLang}", Toast.LENGTH_SHORT)
            .show()
    }

    companion object {
        fun newInstance(): AddLocationFragment{
            return AddLocationFragment()
        }
    }

}