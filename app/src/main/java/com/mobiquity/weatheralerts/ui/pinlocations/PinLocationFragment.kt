package com.mobiquity.weatheralerts.ui.pinlocations

import android.os.Bundle
import androidx.fragment.app.Fragment

class PinLocationFragment: Fragment() {

    companion object{

        @JvmStatic
        fun newInstance():  PinLocationFragment{
            val args = Bundle()

            val fragment = PinLocationFragment()
            fragment.arguments = args
            return fragment
        }
    }
}