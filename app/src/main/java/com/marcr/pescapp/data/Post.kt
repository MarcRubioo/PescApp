package com.marcr.pescapp.data

data class Post(
    val id: String,
    val email: String,
    val imagePost: String,
    val titlePost: String,
    val sitePost: String,
    val categoryPost: String,
    val likes: MutableList<String> = mutableListOf<String>(),
    val comments: MutableList<Comment> = mutableListOf<Comment>(),
)