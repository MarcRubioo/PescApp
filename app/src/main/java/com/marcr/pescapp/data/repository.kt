package com.marcr.pescapp.data

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.play.integrity.internal.i
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class repository {

    companion object {
        fun logUser(user: UserLog, context: Context, callback: (Boolean) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, user.password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            callback(true)
                        } else {
                            callback(false)
                        }
                    }
            }
        }

        fun logoutUser(context: Context, callback: (Boolean) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                FirebaseAuth.getInstance().signOut()
                callback(true)
            }
        }


        fun registerUser(user: User, context: Context, callback: (Boolean) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()
                val storage = FirebaseStorage.getInstance()


                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(user.email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val signInMethods = task.result?.signInMethods ?: emptyList()
                            if (signInMethods.isNotEmpty()) {
                                callback(false)
                                return@addOnCompleteListener
                            }

                            storage.reference.child("defaultprofilephote.png").downloadUrl.addOnSuccessListener { uri ->
                                val imguri = uri.toString()

                                user.password?.let {
                                    FirebaseAuth.getInstance()
                                        .createUserWithEmailAndPassword(user.email, it)
                                        .addOnCompleteListener { createUserTask ->
                                            if (createUserTask.isSuccessful) {
                                                db.collection("users").document(user.email).set(
                                                    hashMapOf(
                                                        "name" to user.name,
                                                        "email" to user.email,
                                                        "password" to user.password,
                                                        "edat" to user.age,
                                                        "img" to imguri,
                                                        "description" to user.description,
                                                        "followers" to user.followersList,
                                                        "following" to user.followingList
                                                    )
                                                )
                                                callback(true)
                                            } else {
                                                callback(false)
                                            }
                                        }
                                }
                            }
                        } else {
                            Toast.makeText(context, "Este email esta en uso ", Toast.LENGTH_SHORT)
                                .show()
                            callback(false)
                        }
                    }
            }
        }

        fun addPost(post: Post, uri: Uri?, context: Context, callback: (Boolean) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()
                val storage =
                    FirebaseStorage.getInstance().reference.child(post.email + "ImagePost")

                uri?.let {
                    storage.putFile(it).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            storage.downloadUrl.addOnSuccessListener { uri ->
                                db.collection("posts").add(
                                    hashMapOf(
                                        "id" to post.id,
                                        "email" to post.email,
                                        "image" to uri.toString(),
                                        "title" to post.titlePost,
                                        "location" to post.sitePost,
                                        "category" to post.categoryPost,
                                        "likes" to post.likes,
                                        "comments" to post.comments
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
        }

        fun modifyDataUser(user: User, uri: Uri?, context: Context, callback: (Boolean) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                val storage = FirebaseStorage.getInstance().reference.child(user.email + "Image")

                if (uri != null) {
                    uri?.let {
                        storage.putFile(it).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                storage.downloadUrl.addOnSuccessListener { uri ->
                                    val userData =
                                        hashMapOf(
                                            "img" to uri.toString(),
                                            "description" to user.description,
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
                        "edat" to user.age,
                        "description" to user.description
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
        }

        fun deleteUserData(email: String, context: Context, callback: (Boolean) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
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
        }

        fun getUserData(email: String, context: Context, callback: (User?) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()
                db.collection("users").document(email).get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val userEmail = document.getString("email") ?: ""
                            val name = document.getString("name") ?: ""
                            val age = document.getString("edat") ?: ""
                            val img = document.getString("img") ?: ""
                            val desc = document.getString("description") ?: ""
                            val followers = document.get("followers") as? List<String> ?: listOf()
                            val following = document.get("following") as? List<String> ?: listOf()

                            val user = User(
                                userEmail,
                                name,
                                "",
                                age,
                                img,
                                desc,
                                followers.toMutableList(),
                                following.toMutableList()
                            )
                            callback(user)
                        } else {
                            callback(null)
                        }
                    }
                    .addOnFailureListener { e ->
                        callback(null)
                    }
            }
        }

        fun getAllPosts(callback: (List<Post>) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            val postsList = mutableListOf<Post>()

            db.collection("posts")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val id = document.getString("id") ?: ""
                        val email = document.getString("email") ?: ""
                        val imagePost = document.getString("image") ?: ""
                        val titlePost = document.getString("title") ?: ""
                        val sitePost = document.getString("location") ?: ""
                        val categoryPost = document.getString("category") ?: ""
                        val likes = document.get("likes") as? List<String> ?: listOf()
                        val comments = document.get("comments") as? List<Comment> ?: listOf()

                        val post = Post(
                            id,
                            email,
                            imagePost,
                            titlePost,
                            sitePost,
                            categoryPost,
                            likes.toMutableList(),
                            comments.toMutableList()
                        )
                        postsList.add(post)
                    }
                    callback(postsList)
                }
                .addOnFailureListener { exception ->

                }
        }

        fun getUsersSearch(userLoged: String, callback: (List<User>) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()
                val userSearchList = mutableListOf<User>()
                db.collection("users")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val email = document.getString("email") ?: ""
                            val name = document.getString("name") ?: ""
                            val password = document.getString("password") ?: ""
                            val age = document.getString("edat") ?: ""
                            val img = document.getString("img") ?: ""
                            val desc = document.getString("description") ?: ""
                            val followers = document.get("followers") as? List<String> ?: listOf()
                            val following = document.get("following") as? List<String> ?: listOf()

                            val user = User(
                                email,
                                name,
                                password,
                                age,
                                img,
                                desc,
                                followers.toMutableList(),
                                following.toMutableList()
                            )
                            if (email != userLoged) {
                                userSearchList.add(user)
                            }
                        }
                        callback(userSearchList)
                    }
                    .addOnFailureListener { exception ->

                    }
            }
        }


        fun getPostProfile(callback: (List<Post>) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

            if (currentUserEmail != null) {
                val postsList = mutableListOf<Post>()

                db.collection("posts")
                    .whereEqualTo("email", currentUserEmail)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val id = document.getString("id") ?: ""
                            val email = document.getString("email") ?: ""
                            val imagePost = document.getString("image") ?: ""
                            val titlePost = document.getString("title") ?: ""
                            val sitePost = document.getString("location") ?: ""
                            val categoryPost = document.getString("category") ?: ""
                            val likes = document.get("likes") as? List<String> ?: listOf()

                            val post = Post(
                                id,
                                email,
                                imagePost,
                                titlePost,
                                sitePost,
                                categoryPost,
                                likes.toMutableList()
                            )
                            postsList.add(post)
                        }
                        callback(postsList)
                    }
                    .addOnFailureListener { exception ->

                    }
            }
        }


        fun getPostProfileSearch(email: String, callback: (List<Post>) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            if (email != null) {
                val postsList = mutableListOf<Post>()

                db.collection("posts")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val id = document.getString("id") ?: ""
                            val email = document.getString("email") ?: ""
                            val imagePost = document.getString("image") ?: ""
                            val titlePost = document.getString("title") ?: ""
                            val sitePost = document.getString("location") ?: ""
                            val categoryPost = document.getString("category") ?: ""
                            val likes = document.get("likes") as? List<String> ?: listOf()

                            val post = Post(
                                id,
                                email,
                                imagePost,
                                titlePost,
                                sitePost,
                                categoryPost,
                                likes.toMutableList()
                            )
                            postsList.add(post)
                        }
                        callback(postsList)
                    }
                    .addOnFailureListener { exception ->
                    }
            }
        }


        fun addFollowerAndFollowing(
            emailToFollow: String,
            emailUserLoged: String,
            callback: (Boolean) -> Unit
        ) {
            GlobalScope.launch(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()

                db.collection("users")
                    .document(emailToFollow)
                    .update("followers", FieldValue.arrayUnion(emailUserLoged))
                    .addOnSuccessListener {

                        db.collection("users")
                            .document(emailUserLoged)
                            .update("following", FieldValue.arrayUnion(emailToFollow))
                            .addOnSuccessListener {
                                callback(true)
                            }
                            .addOnFailureListener {
                                callback(false)
                            }
                    }
                    .addOnFailureListener {
                        callback(false)
                    }
            }
        }

        fun removeFollowerAndFollowing(
            emailToUnfollow: String,
            emailUserLoged: String,
            callback: (Boolean) -> Unit
        ) {
            GlobalScope.launch(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()

                db.collection("users")
                    .document(emailToUnfollow)
                    .update("followers", FieldValue.arrayRemove(emailUserLoged))
                    .addOnSuccessListener {

                        db.collection("users")
                            .document(emailUserLoged)
                            .update("following", FieldValue.arrayRemove(emailToUnfollow))
                            .addOnSuccessListener {
                                callback(true)
                            }
                            .addOnFailureListener {
                                callback(false)
                            }
                    }
                    .addOnFailureListener {
                        callback(false)
                    }
            }
        }

        fun checkIfUserIsFollower(
            emailToUnfollow: String,
            emailUserLoged: String,
            callback: (Boolean) -> Unit
        ) {
            GlobalScope.launch(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()

                db.collection("users")
                    .document(emailToUnfollow)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val followers = document.get("followers") as? List<String>
                            val isFollower = followers?.contains(emailUserLoged) ?: false
                            callback(isFollower)
                        } else {
                            callback(false)
                        }
                    }
                    .addOnFailureListener {
                        callback(false)
                    }
            }
        }

        fun addLikeToPost(
            idPost: String,
            emailUserLoged: String,
            callback: (Boolean) -> Unit
        ) {
            GlobalScope.launch(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()

                db.collection("posts")
                    .whereEqualTo("id", idPost)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val postDocRef = db.collection("posts").document(document.id)
                            postDocRef.update("likes", FieldValue.arrayUnion(emailUserLoged))
                                .addOnSuccessListener {
                                    callback(true)
                                }
                                .addOnFailureListener {
                                    callback(false)
                                }
                        }
                    }
                    .addOnFailureListener {
                        callback(false)
                    }
            }
        }


        fun removeLikeToPost(
            idPost: String,
            emailUserLoged: String,
            callback: (Boolean) -> Unit
        ) {
            GlobalScope.launch(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()

                db.collection("posts")
                    .whereEqualTo("id", idPost)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val postDocRef = db.collection("posts").document(document.id)
                            postDocRef.update("likes", FieldValue.arrayRemove(emailUserLoged))
                                .addOnSuccessListener {
                                    callback(true)
                                }
                                .addOnFailureListener {
                                    callback(false)
                                }
                        }
                    }
                    .addOnFailureListener {
                        callback(false)
                    }
            }
        }

        fun getCommentsByPostId(postId: String, callback: (List<Comment>) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            val postsCollection = db.collection("posts")

            postsCollection.whereEqualTo("id", postId).get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val documentSnapshot = querySnapshot.documents[0]
                        val postRef = documentSnapshot.reference

                        postRef.get().addOnSuccessListener { document ->
                            if (document.exists()) {
                                val comments =
                                    document.get("comments") as? List<Map<String, String>>
                                        ?: emptyList()
                                val commentList = comments.map {
                                    Comment(it["email"] ?: "", it["description"] ?: "")
                                }
                                callback(commentList)
                            } else {
                                callback(emptyList())
                            }
                        }.addOnFailureListener {
                            callback(emptyList())
                        }
                    } else {
                        callback(emptyList())
                    }
                }.addOnFailureListener {
                    callback(emptyList())
                }
        }


        fun addComment(postId: String, comment: Comment, callback: (Boolean) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()
                val postsCollection = db.collection("posts")

                postsCollection.whereEqualTo("id", postId).get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val documentSnapshot = querySnapshot.documents[0]
                            val postRef = documentSnapshot.reference

                            db.runTransaction { transaction ->
                                val snapshot = transaction.get(postRef)

                                if (snapshot.exists()) {
                                    val comments =
                                        snapshot.get("comments") as? MutableList<Map<String, String>>
                                            ?: mutableListOf()

                                    comments.add(comment.toMap())

                                    transaction.update(postRef, "comments", comments)
                                    true
                                } else {
                                    false
                                }
                            }.addOnSuccessListener {
                                callback(true)
                            }.addOnFailureListener {
                                callback(false)
                            }
                        } else {
                            callback(false)
                        }
                    }.addOnFailureListener {
                        callback(false)
                    }
            }
        }

    }
}