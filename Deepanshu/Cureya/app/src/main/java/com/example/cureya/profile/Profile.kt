package com.example.cureya.profile

import java.util.*

data class Profile(
    val photoUrl: String = "https://firebasestorage.googleapis.com/v0/b/cureyadraft.appspot.com/o/static%2Fdefault_profile_pic.png?alt=media&token=a52249e9-4de2-4e67-8e73-fa71ff26f289",
    val email: String = "Not provided",
    val gender: String = "Not provided",
    val joinedGroups: Int = 0,
    val joinedCureya: Date = Date(),
    val about: String = ""
)