package com.marcr.pescapp.commnentsPost

import androidx.lifecycle.ViewModel
import com.marcr.pescapp.data.Comment
import com.marcr.pescapp.data.repository

class ViewModelCommentsPost : ViewModel() {

    fun getCommentsByPostId(postId: String, callback: (List<Comment>) -> Unit) {
        repository.getCommentsByPostId(postId) { comments ->
            callback(comments)
        }
    }

    fun addComment(postId: String, email: String, description: String, callback: (Boolean) -> Unit) {
        val comment = Comment(email, description)

        repository.addComment(postId, comment) { success ->
            callback(success)
        }
    }
}
