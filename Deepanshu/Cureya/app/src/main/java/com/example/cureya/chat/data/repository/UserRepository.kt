package com.example.cureya.chat.data.repository

import androidx.lifecycle.LiveData
import com.example.cureya.chat.data.models.User

interface UserRepository {
    fun getAllUsers(): LiveData<List<User>>
    suspend fun getUserById(userId: String): User?
}