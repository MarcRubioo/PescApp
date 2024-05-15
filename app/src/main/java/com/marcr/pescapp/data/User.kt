package com.marcr.pescapp.data

class User (val email: String,
            val name: String,
            val password: String? = null,
            val age: String,
            val img: String = "",
            val description: String="",
            val followersList: MutableList<String> = mutableListOf<String>(),
            val followingList: MutableList<String> = mutableListOf<String>()

)