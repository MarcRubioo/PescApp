package com.marcr.pescapp.buscador

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.data.User
import com.marcr.pescapp.data.repository

class ViewModelBuscador: ViewModel() {
    private var _usersSearch= MutableLiveData<MutableList<User>>()
    val usersSearch : LiveData<MutableList<User>> = _usersSearch

    fun getUserSearch() {
        repository.getUsersSearch { userSearchList ->
            _usersSearch.value = userSearchList.toMutableList()
        }
    }
}