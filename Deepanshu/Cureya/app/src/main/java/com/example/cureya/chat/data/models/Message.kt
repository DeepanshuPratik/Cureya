package com.example.cureya.chat.data.models

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*


/*
* A message can be a text message or an image
*   message
*       - senderID
*       - receiverID
*       - text
*       - photoUrl
*       - createdAt
*/

@IgnoreExtraProperties
data class Message(
    val senderId: String? = null,
    val receiverId: String? = null,
    val text: String? = null,
    val photoUrl: String? = null,
    val createdAt: Date = Date()
)