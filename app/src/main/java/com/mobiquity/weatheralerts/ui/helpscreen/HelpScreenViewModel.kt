package com.mobiquity.weatheralerts.ui.helpscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HelpScreenViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is HelpScreen Fragment"
    }
    val text: LiveData<String> = _text
}