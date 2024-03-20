package com.marcr.pescapp.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcr.pescapp.data.UserLog
import com.marcr.pescapp.data.repository

class ViewModelLogin: ViewModel() {
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    fun loginUser(context: Context, email: String, password: String) {
        val user = UserLog(email, password)
        repository.logUser(user, context) { success ->
            _loginSuccess.postValue(success)
        }
    }
}