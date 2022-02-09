package com.example.cureya.community.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
data class Post(
    val postId: String = "",
    val caption: String = "",
    val photoUrl: String = "",
    val commentCount: Int = 0,
    val createdAt: Date = Date(),
    val likes: List<String> = listOf(),
    val shares: Int = 0,
    val userId: String = "",
    val profilePhoto: String = "",
    val userName: String = "",
    val tags: List<TAG> = listOf()
) : Parcelable

@Parcelize
enum class TAG : Parcelable {
    STRESS, ANXIETY, PARANOIA, PSYCHOSIS, DEPRESSION,
}