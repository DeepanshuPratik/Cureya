package com.example.cureya.chat.data.models

import java.io.Serializable


class ChatUser(
    val lastMessage: Message,
    user: User
) : User(user.userId, user.name, user.photoUrl, user.isCounselor)
