package com.example.cureya.chat.data.models

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize
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

@Parcelize
@IgnoreExtraProperties
data class Message(
    val senderId: String? = null,
    val receiverId: String? = null,
    val text: String? = null,
    val photoUrl: String? = null,
    val createdAt: Date = Date()
) : Parcelable