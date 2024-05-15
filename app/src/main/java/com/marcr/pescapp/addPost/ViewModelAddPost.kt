package com.marcr.pescapp.addPost

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.data.repository

class ViewModelAddPost: ViewModel()  {

    private var imagePost: Uri? = null

    fun setImageUri(uri: Uri?) {
        imagePost = uri
    }

    fun addPostViewModel(context: Context,id: String, email: String, titlePost: String, sitePost: String, categoryPost: String, callback: (Boolean) -> Unit) {
        val image = imagePost?.toString() ?: ""

        val post = Post(id,email, image, titlePost, sitePost, categoryPost )
        repository.addPost(post,imagePost, context) { success ->
            callback(success)
        }
    }

    fun generarCodigoAleatorio(): String {
        val caracteres = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val codigo = StringBuilder(20)

        repeat(20) {
            val caracterAleatorio = caracteres.random()
            codigo.append(caracterAleatorio)
        }

        return codigo.toString()
    }
}