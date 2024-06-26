package com.marcr.pescapp.perfil

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.data.User
import com.marcr.pescapp.data.repository

class ViewModelPerfil: ViewModel() {
    fun getUserProfile(email: String, context: Context, callback: (User?) -> Unit) {
        repository.getUserData(email, context) { user ->
            callback(user)
        }
    }

    private var _postProfile= MutableLiveData<MutableList<Post>>()
    val postProfile : LiveData<MutableList<Post>> = _postProfile

    fun getUserPosts() {
        repository.getPostProfile { postProfileList ->
            _postProfile.value = postProfileList.toMutableList()
        }
    }

}