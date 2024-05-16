package com.marcr.pescapp.data

class Comment (
    val emailUser: String,
    val textComment: String,
)
{
    fun toMap(): Map<String, String> {
        return mapOf(
            "email" to emailUser,
            "description" to textComment
        )
    }
}
