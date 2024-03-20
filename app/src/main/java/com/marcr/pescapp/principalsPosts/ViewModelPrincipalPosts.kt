package com.marcr.pescapp.principalsPosts

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.data.repository

class ViewModelPrincipalPosts: ViewModel() {
    private var _posts= MutableLiveData<MutableList<Post>>()
    val posts : LiveData<MutableList<Post>> = _posts

    fun getPosts() {
        repository.getAllPosts { postsList ->
            _posts.value = postsList.toMutableList()
        }
    }
}