package com.example.cureya.community.models

import java.util.*


data class Post(
    val postId:String = "",
    val caption: String ="",
    val photoUrl: String="",
    val comments: List<String> = listOf(),
    val createdAt: Date = Date(),
    val likes: List<String> = listOf(),
    val shares: Int = 0,
    val userId: String = "",
    val profilePhoto: String="",
    val userName: String="",
    val tags: List<TAG> = listOf()
)

enum class TAG {
    STRESS, ANXIETY, PARANOIA, PSYCHOSIS,DEPRESSION,
}