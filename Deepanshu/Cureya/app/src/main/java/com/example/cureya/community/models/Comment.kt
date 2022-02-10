package com.example.cureya.community.models

import com.example.cureya.community.utils.defaultProfilePic
import java.util.*

class Comment(
    val text: String = "",
    val userId: String = "",
    val userName: String = "",
    val photoUrl: String = defaultProfilePic,
    val createdAt: Date = Date()
)