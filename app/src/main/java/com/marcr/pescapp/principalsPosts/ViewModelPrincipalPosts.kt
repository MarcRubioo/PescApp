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
    private val _likeResult = MutableLiveData<Boolean>()
    val likeResult: LiveData<Boolean> = _likeResult
    fun AddLike(idPost: String, emailUserLoged: String) {
        repository.addLikeToPost(idPost, emailUserLoged) { success ->
            _likeResult.postValue(success)
        }
    }

    private val _removeLike = MutableLiveData<Boolean>()
    val removeLike: LiveData<Boolean> = _removeLike

    fun RemoveLike(idPost: String, emailUserLoged: String) {
        repository.removeLikeToPost(idPost, emailUserLoged) { success ->
            _removeLike.postValue(success)
        }
    }

    private val _isLike = MutableLiveData<Boolean>()
    val isLike: LiveData<Boolean> = _isLike
    fun checkIfIsLike(idPost: String, emailUserLoged: String) {
        repository.checkIfPostHaveFollowOfUserCurrent(idPost, emailUserLoged) { isFollower ->
            _isLike.postValue(isFollower)
        }
    }

}