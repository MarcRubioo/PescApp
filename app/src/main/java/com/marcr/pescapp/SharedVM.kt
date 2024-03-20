package com.marcr.pescapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedVM : ViewModel() {

    val user = MutableLiveData<String>()

    fun userLoged(text: String) {
        user.value = text
    }
}