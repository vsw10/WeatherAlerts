package com.mobiquity.weatheralerts.ui.bookmarkedlocations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookmarkedViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Bookmarked Locations"
    }
    val text: LiveData<String> = _text
}