package com.example.cureya.chat.ui.fragments.chatList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cureya.chat.data.models.Message
import com.example.cureya.chat.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatListViewModel() : ViewModel() {

    private val allUsers = MutableLiveData<List<User>>()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database =
        FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val users = database.child("users").get()
                .await().children.map {
                    val user = it.getValue(User::class.java)!!
                    user.userId = it.key
                    user
                }.filter { it.userId != auth.uid }
            users.forEach {
                it.lastMessage = getLastMessage(it.userId!!)
            }
            allUsers.value = users
            listenForUsersValueChanges()
        }
    }

    fun getAllUsers(): LiveData<List<User>> = allUsers

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
                        }.filter { it.userId != auth.uid }
                    allUsers.postValue(dbUsers)
                    loadLastMessages()
                } else {
                    allUsers.postValue(emptyList())
                }
            }
        }
        database.child("users").addValueEventListener(userValueListener)
    }

    private fun loadLastMessages() {
        viewModelScope.launch {
            val users = allUsers.value?.map {
                it.lastMessage = getLastMessage(it.userId!!)
                it
            } ?: listOf()
            allUsers.value = users
        }
    }

    suspend fun getUserById(userId: String): User =
        database.child("users").child(userId).get().await().getValue(User::class.java)!!

    private suspend fun getLastMessage(receiverId: String): Message? {
        return database.child("last_message").child(getChatId(receiverId)).get().await()
            .getValue(Message::class.java)
    }

    private fun getChatId(receiverId: String) =
        if (auth.uid!! > receiverId) auth.uid!! + receiverId else receiverId + auth.uid;

    companion object {
        const val TAG = "CHAT_LIST_VIEW_MODEL"
    }

}