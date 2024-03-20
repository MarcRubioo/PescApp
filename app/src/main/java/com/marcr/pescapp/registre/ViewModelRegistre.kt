package com.marcr.pescapp.registre

import android.content.Context
import androidx.lifecycle.ViewModel
import com.marcr.pescapp.data.repository
import com.marcr.pescapp.data.User

class ViewModelRegistre: ViewModel() {
    fun registerUser(context: Context, email: String, name: String, password: String, age: String, img: String, callback: (Boolean) -> Unit) {
        val user = User(email, name, password, age, img)
        repository.registerUser(user, context) { success ->
            callback(success)
        }
    }
}