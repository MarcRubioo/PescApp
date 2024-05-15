package com.marcr.pescapp.data

class User (val email: String,
            val name: String,
            val password: String? = null,
            val age: String,
            val img: String = "",
            val description: String="",
            val followers: String = "0",
            val following: String="0")