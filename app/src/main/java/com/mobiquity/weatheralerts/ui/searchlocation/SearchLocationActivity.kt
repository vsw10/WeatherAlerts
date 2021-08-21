package com.mobiquity.weatheralerts.ui.searchlocation

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.mobiquity.weatheralerts.MainActivity
import com.mobiquity.weatheralerts.R
import com.mobiquity.weatheralerts.databinding.ToolbarSearchTextBinding

class SearchLocationActivity: MainActivity() {

    lateinit var toolbarSearchTextBinding: ToolbarSearchTextBinding

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(activityBaseBinding.toolbar)
        val searchLayout: View =
            layoutInflater.inflate(R.layout.toolbar_search_text,
                activityBaseBinding.toolbar)
        activityBaseBinding.toolbar.isVisible = true
        val searchEditText: EditText = searchLayout.findViewById<EditText>(R.id.search_edit_text)
        searchEditText?.let {
            searchEditText.addTextChangedListener(enterSearchTextWatcher)
        }
        val searchButton: AppCompatImageButton =
            searchLayout.findViewById<AppCompatImageButton>(R.id.search_button)

        searchButton.setOnClickListener(View.OnClickListener {

            Log.d("vs","Searched Text ${searchEditText.text}")
        })
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

        //searchEditTextView.
        //toolbarSearchTextBinding = ToolbarSearchTextBinding.bind(s)
        //toolbarSearchTextBinding.searchEditText.addTextChangedListener()
    }

    private val enterSearchTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            Log.d("vs","Entered Text $s")
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
}