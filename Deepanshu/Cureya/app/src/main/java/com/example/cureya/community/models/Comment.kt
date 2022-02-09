package com.example.cureya.community.models

import java.util.*

class Comment(
    val text: String = "",
    val userId: String = "",
    val userName: String = "",
    val photoUrl: String = "",
    val createdAt: Date = Date()
)