package com.marcr.pescapp.perfilDades

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.marcr.pescapp.data.User
import com.marcr.pescapp.data.repository

class ViewModelPerfilDades: ViewModel() {

    private var imageProfile: Uri? = null

    fun setImageUri(uri: Uri?) {
        imageProfile = uri
    }

    fun getUserData(email: String, context: Context, callback: (User?) -> Unit) {
        repository.getUserData(email, context) { user ->
            callback(user)
        }
    }

    fun modifyDataUser(context: Context, email: String, name: String, password: String?, age: String, callback: (Boolean) -> Unit) {
        val image = imageProfile?.toString() ?: ""

        val user = User(email, name, password ?: "", age, image)
        repository.modifyDataUser(user, imageProfile, context) { success ->
            callback(success)
        }
    }

    fun deleteUser(context: Context, email: String, callback: (Boolean) -> Unit) {
        repository.deleteUserData(email, context) { success ->
            callback(success)
        }
    }
}
