package com.mobiquity.weatheralerts.ui.helpscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mobiquity.weatheralerts.databinding.FragmentHelpScreenBinding


class HelpScreenFragment : Fragment() {

    private lateinit var helpScreenViewModel: HelpScreenViewModel
    private var fragmentHelpScreenBinding: FragmentHelpScreenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = fragmentHelpScreenBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        helpScreenViewModel =
            ViewModelProvider(this).get(HelpScreenViewModel::class.java)

        fragmentHelpScreenBinding = FragmentHelpScreenBinding
            .inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHelpScreenBinding = null
    }
}