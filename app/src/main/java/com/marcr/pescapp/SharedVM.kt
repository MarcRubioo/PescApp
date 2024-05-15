package com.marcr.pescapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedVM : ViewModel() {

    private val _userSearch = MutableLiveData<String>()
    val userSearch: LiveData<String> = _userSearch

    fun setUserSearch(email: String) {
        _userSearch.value = email
    }
}