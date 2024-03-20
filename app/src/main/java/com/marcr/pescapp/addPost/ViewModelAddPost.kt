package com.marcr.pescapp.addPost

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.data.repository

class ViewModelAddPost: ViewModel()  {

    private var imagePost: Uri? = null

    fun setImageUri(uri: Uri) {
        imagePost = uri
    }

    fun addPostViewModel(context: Context, email: String, titlePost: String, sitePost: String, categoryPost: String, callback: (Boolean) -> Unit) {
        val image = imagePost?.toString() ?: ""

        val post = Post(email, image, titlePost, sitePost, categoryPost)
        repository.addPost(post,imagePost, context) { success ->
            callback(success)
        }
    }
}