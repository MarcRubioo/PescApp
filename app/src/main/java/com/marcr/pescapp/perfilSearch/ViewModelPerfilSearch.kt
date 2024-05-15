package com.marcr.pescapp.perfilSearch

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.data.User
import com.marcr.pescapp.data.repository

class ViewModelPerfilSearch: ViewModel() {
    fun getUserProfile(email: String, context: Context, callback: (User?) -> Unit) {
        repository.getUserData(email, context) { user ->
            callback(user)
        }
    }

    private var _postProfile= MutableLiveData<MutableList<Post>>()
    val postProfile : LiveData<MutableList<Post>> = _postProfile

    fun getUserPosts(email: String) {
        repository.getPostProfileSearch(email) { postProfileList ->
            _postProfile.value = postProfileList.toMutableList()
        }
    }

    private val _followResult = MutableLiveData<Boolean>()
    val followResult: LiveData<Boolean> = _followResult

    fun followUser(emailToFollow: String, emailUserLoged: String) {
        repository.addFollowerAndFollowing(emailToFollow, emailUserLoged) { success ->
            _followResult.postValue(success)
        }
    }

    private val _unfollowResult = MutableLiveData<Boolean>()
    val unfollowResult: LiveData<Boolean> = _unfollowResult

    fun unfollowUser(emailToFollow: String, emailUserLoged: String) {
        repository.removeFollowerAndFollowing(emailToFollow, emailUserLoged) { success ->
            _unfollowResult.postValue(success)
        }
    }

    private val _isFollower = MutableLiveData<Boolean>()
    val isFollower: LiveData<Boolean> = _isFollower
    fun checkIfUserIsFollower(emailToCheck: String, emailUserLoged: String) {
        repository.checkIfUserIsFollower(emailToCheck, emailUserLoged) { isFollower ->
            _isFollower.postValue(isFollower)
        }
    }


}