package com.example.cureya.chat.data.repository

import androidx.lifecycle.LiveData
import com.example.cureya.chat.data.models.Chat

interface ChatRepository {
//    fun getChatById(chatId: String): LiveData<Chat>
    fun getAllChats(): LiveData<List<Chat>>
//    fun sendMessage(message: String, receiverId: String)
//    fun getChats(): LiveData<List<String>>
//    fun getChat(receiverId: String): LiveData<Chat>
}