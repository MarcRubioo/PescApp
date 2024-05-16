package com.marcr.pescapp.principalsPosts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.data.repository

class ViewModelPrincipalPosts : ViewModel() {
    private var _posts = MutableLiveData<MutableList<Post>>()
    val posts: LiveData<MutableList<Post>> = _posts

    fun getPosts() {
        repository.getAllPosts { postsList ->
            _posts.value = postsList.toMutableList()
        }
    }

    private val _likeResult = MutableLiveData<Pair<Post?, Int?>>()
    val likeResult: LiveData<Pair<Post?, Int?>> = _likeResult

    fun toggleLike(postId: String, emailUserLogged: String, position: Int) {
        val post = _posts.value?.find { it.id == postId }
        post?.let {
            if (it.likes.contains(emailUserLogged)) {
                repository.removeLikeToPost(postId, emailUserLogged) { success ->
                    if (success) {
                        it.likes.remove(emailUserLogged)
                        _likeResult.postValue(Pair(it, position))
                    }
                }
            } else {
                repository.addLikeToPost(postId, emailUserLogged) { success ->
                    if (success) {
                        it.likes.add(emailUserLogged)
                        _likeResult.postValue(Pair(it, position))
                    }
                }
            }
        }
    }
}
