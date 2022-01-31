package com.example.cureya.chat.data.models

import com.google.firebase.database.IgnoreExtraProperties


/*
* Data class representing a user in chats
* For chat we need the profile photo of user,
* name and the status, if user is a counselor or not
*/

@IgnoreExtraProperties
data class User(
    val userId: String? = null,
    val name: String? = null,
    val photoUrl: String? = null,
    val isCounselor: Boolean = false
)