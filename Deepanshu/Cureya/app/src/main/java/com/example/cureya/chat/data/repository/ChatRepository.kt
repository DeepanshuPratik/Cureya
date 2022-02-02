package com.example.cureya.chat.data.repository

import androidx.lifecycle.LiveData
import com.example.cureya.chat.data.models.Chat
import com.example.cureya.chat.data.models.ChatUser
import com.example.cureya.chat.data.models.Message

interface ChatRepository {
    suspend fun getAllChatUsers(): LiveData<List<ChatUser>>
    suspend fun getChatByReceiverId(receiverId: String): LiveData<Chat>
    suspend fun sendMessage(message: Message)
}