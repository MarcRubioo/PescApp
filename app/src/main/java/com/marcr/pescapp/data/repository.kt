package com.marcr.pescapp.data

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.play.integrity.internal.i
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class repository {

    companion object {
        fun logUser(user: UserLog, context: Context, callback: (Boolean) -> Unit) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback(true)
                    } else {
                        callback(false)
                    }
                }
        }


        fun registerUser(user: User, context: Context, callback: (Boolean) -> Unit) {
            var imguri: String
            val db = FirebaseFirestore.getInstance()
            val storage = FirebaseStorage.getInstance()
            storage.reference.child("defaultprofilephote.png").downloadUrl.addOnSuccessListener { uri ->
                imguri = uri.toString()

                if (imguri != null) {
                    user.password?.let {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email, it)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    db.collection("users").document(user.email).set(
                                        hashMapOf(
                                            "name" to user.name,
                                            "email" to user.email,
                                            "password" to user.password,
                                            "edat" to user.age,
                                            "img" to imguri
                                        )
                                    )
                                    callback(true)
                                } else {
                                    callback(false)
                                }
                            }
                    }
                }
            }
        }

        fun addPost(post: Post, uri: Uri?, context: Context, callback: (Boolean) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            val storage = FirebaseStorage.getInstance().reference.child(post.email + "ImagePost")

            uri?.let {
                storage.putFile(it).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        storage.downloadUrl.addOnSuccessListener { uri ->
                            db.collection("posts").add(
                                hashMapOf(
                                    "email" to post.email,
                                    "image" to uri.toString(),
                                    "title" to post.titlePost,
                                    "location" to post.sitePost,
                                    "category" to post.categoryPost
                                )
                            )
                                .addOnSuccessListener {
                                    callback(true)
                                }
                                .addOnFailureListener { e ->
                                    callback(false)
                                }
                        }
                    }
                }
            }
        }

        fun modifyDataUser(user: User, uri: Uri?, context: Context, callback: (Boolean) -> Unit) {
            val storage = FirebaseStorage.getInstance().reference.child(user.email + "Image")

            if (uri != null) {
                uri?.let {
                    storage.putFile(it).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            storage.downloadUrl.addOnSuccessListener { uri ->
                                val userData =
                                    hashMapOf(
                                        "img" to uri.toString(),
                                        "name" to user.name,
                                        "edat" to user.age
                                    )


                                val db = FirebaseFirestore.getInstance()

                                db.collection("users").document(user.email)
                                    .update(userData as Map<String, Any>)
                                    .addOnSuccessListener {
                                        callback(true)
                                    }
                                    .addOnFailureListener { e ->
                                        callback(false)
                                    }
                            }
                        }
                    }
                }
            } else {
                val userData2 = hashMapOf(
                    "name" to user.name,
                    "edat" to user.age
                )

                val db = FirebaseFirestore.getInstance()

                db.collection("users").document(user.email)
                    .update(userData2 as Map<String, Any>)
                    .addOnSuccessListener {
                        callback(true)
                    }
                    .addOnFailureListener { e ->
                        callback(false)
                    }
            }

        }

        fun deleteUserData(email: String, context: Context, callback: (Boolean) -> Unit) {
            val auth = FirebaseAuth.getInstance()
            val db = FirebaseFirestore.getInstance()

            auth.currentUser?.delete()?.addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    db.collection("users").document(email)
                        .delete()
                        .addOnSuccessListener {
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            callback(false)
                        }
                } else {
                    callback(false)
                }
            }
        }

        fun getUserData(email: String, context: Context, callback: (User?) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(email).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userEmail = document.getString("email") ?: ""
                        val name = document.getString("name") ?: ""
                        val age = document.getString("edat") ?: ""
                        val img = document.getString("img") ?: ""

                        val user = User(userEmail, name, "", age, img)
                        callback(user)
                    } else {
                        callback(null)
                    }
                }
                .addOnFailureListener { e ->
                    callback(null)
                }
        }

        fun getAllPosts(callback: (List<Post>) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            val postsList = mutableListOf<Post>()
            db.collection("posts")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val post = Post(
                            document.getString("email") ?: "",
                            document.getString("image") ?: "",
                            document.getString("title") ?: "",
                            document.getString("location") ?: "",
                            document.getString("category") ?: ""
                        )
                        postsList.add(post)
                    }
                    callback(postsList)
                }
                .addOnFailureListener { exception ->
                    // Manejar el error aquí
                }
        }

    }
}