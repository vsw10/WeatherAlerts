package com.mobiquity.weatheralerts.ui.bookmarkedlocations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mobiquity.weatheralerts.R
import com.mobiquity.weatheralerts.databinding.FragmentBookmarkedBinding
import com.mobiquity.weatheralerts.extras.LocationConstants
import com.mobiquity.weatheralerts.extras.WeatherConstants
import com.mobiquity.weatheralerts.ui.searchlocation.SearchLocationFragent

class BookmarkedFragment : Fragment() {

    private lateinit var bookmarkedViewModel: BookmarkedViewModel
    private var fragmentBookmarkedBinding: FragmentBookmarkedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = fragmentBookmarkedBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bookmarkedViewModel =
            ViewModelProvider(this).get(BookmarkedViewModel::class.java)

        fragmentBookmarkedBinding = FragmentBookmarkedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setUiValues(root)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBookmarkedBinding = null
    }

    companion object {

        lateinit var bundleValues : Bundle
        @JvmStatic
        fun newInstance(bundle: Bundle): BookmarkedFragment {
            bundleValues = bundle
            return BookmarkedFragment()
        }
    }


    fun setUiValues(root:View){

        var cityName : String? = bundleValues.getString(LocationConstants.CITY_NAME)
        root.findViewById<TextView>(R.id.city_name)?.setText(cityName)
        var tempToday: String? =  bundleValues.getString(WeatherConstants.TEMPERATURE)
        var convertTemp: Double? = (tempToday?.toDouble()?.div(15) ?: 27) as Double?
        root.findViewById<TextView>(R.id.temperatureToday)?.setText("temp ${convertTemp?.toInt()} C")

        var feelsLike:String? = bundleValues.getString(WeatherConstants.FEELS_LIKE)
        var convertTempFeelsLike: Double? = (feelsLike?.toDouble()?.div(15) ?: 27) as Double?
        root.findViewById<TextView>(R.id.feels_like)?.setText("Feels Like : ${convertTempFeelsLike?.toInt()} C")
        var minTemp:String? =  bundleValues.getString(WeatherConstants.MIN_TEMPERATURE)
        var convertMinTemp: Double? = (minTemp?.toDouble()?.div(15) ?: 27) as Double?
        root.findViewById<TextView>(R.id.min_temp)?.setText("Min Temp : ${convertMinTemp?.toInt()} C")
        var maxTemp:String? =  bundleValues.getString(WeatherConstants.MAX_TEMPERATURE)
        var convertMaxTemp: Double? = (maxTemp?.toDouble()?.div(15) ?: 27) as Double?
        root.findViewById<TextView>(R.id.max_temp)?.setText("Max Temp : ${convertMaxTemp?.toInt()} C")
        var humidity:String? = bundleValues.getString(WeatherConstants.HUMIDITY)

        var country:String? = bundleValues.getString(WeatherConstants.COUNTRY)
        root.findViewById<TextView>(R.id.country)?.setText("Country : $country")
        var rain:String? = bundleValues.getString(WeatherConstants.RAIN)
        if(rain.isNullOrEmpty()){
            rain = "No Rain"
        }
        root.findViewById<TextView>(R.id.rain)?.setText("Current Weather : ${rain.uppercase()}")
        var visibility:String? = bundleValues.getString(WeatherConstants.VISIBILITY)
        root.findViewById<TextView>(R.id.visibility)?.setText("Visibility : $visibility")
    }


}