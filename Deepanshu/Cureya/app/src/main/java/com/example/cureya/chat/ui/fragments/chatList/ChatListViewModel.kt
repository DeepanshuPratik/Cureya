package com.example.cureya.chat.ui.fragments.chatList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cureya.chat.data.models.ChatUser
import com.example.cureya.chat.data.models.Message
import com.example.cureya.chat.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatListViewModel() : ViewModel() {

    private val chatUsers: MutableLiveData<List<ChatUser>> = MutableLiveData(listOf())
    private val allUsers = MutableLiveData<List<User>>()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database =
        FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference

    init {
        loadData()
    }

    fun getAllChatUsers(): LiveData<List<ChatUser>> {
        viewModelScope.launch {
            listenForChatUsersValueChanges()
        }
        return chatUsers
    }

    private fun loadData() {
        viewModelScope.launch {
            allUsers.value = database.child("users").get()
                .await().children.map {
                    val user = it.getValue(User::class.java)!!
                    user.userId = it.key
                    user
                }
            listenForUsersValueChanges()
        }
    }

    fun getAllUsers(): LiveData<List<User>> = allUsers

    private suspend fun listenForChatUsersValueChanges() {
        val userValueListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    GlobalScope.launch {
                        val chatUserList = dataSnapshot.children.map {
                            val user = getUserById(it.key!!)
                            val message = it.getValue(Message::class.java)!!
                            ChatUser(message, user)
                        }
                        chatUsers.postValue(chatUserList)
                    }
                } else {
                    chatUsers.postValue(emptyList())
                }
            }
        }
        database.child("chat_users").child(auth.uid!!).addValueEventListener(userValueListener)
    }

    private fun listenForUsersValueChanges() {
        val userValueListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dbUsers = dataSnapshot.children
                        .mapNotNull {
                            val user = it.getValue(User::class.java)!!
                            user.userId = it.key
                            user
                        }.toList()
                    allUsers.postValue(dbUsers)
                } else {
                    allUsers.postValue(emptyList())
                }
            }
        }
        database.child("users").addValueEventListener(userValueListener)
    }

    suspend fun getUserById(userId: String): User =
        database.child("users").child(userId).get().await().getValue(User::class.java)!!


}