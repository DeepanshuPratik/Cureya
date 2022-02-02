package com.example.cureya.chat.ui.fragments.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cureya.chat.data.models.Chat
import com.example.cureya.chat.data.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database =
        FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference

    private val chat = MutableLiveData<Chat>()

    fun getChats(): LiveData<Chat> {
        return chat
    }

    fun loadChats(receiverId: String) {
        viewModelScope.launch {
            val messages =
                database.child("chats").child(getChatId(receiverId)).child("messages").get()
                    .await().children.map { it.getValue(Message::class.java)!! }
            chat.value = Chat(messages)
            listenForChatValueChanges(receiverId)
        }
    }

    suspend fun sendMessage(text: String, receiverId: String) {
        val message = Message(text = text, senderId = auth.uid!!, receiverId = receiverId)
        database.child("chats").child(getChatId(receiverId)).child("messages").push()
            .setValue(message).await()
        database.child("chat_users").child(auth.uid!!).child(message.receiverId!!)
            .child("lastMessage").setValue(message).await()
    }

    private fun listenForChatValueChanges(receiverId: String) {
        val chatValueListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val messages =
                        dataSnapshot.child("messages").children.map { it.getValue(Message::class.java)!! }
                    val userChat = Chat(messages)
                    chat.value = userChat
                } else {
                    chat.value = Chat()
                }
            }
        }
        database.child("chats").child(getChatId(receiverId))
            .addValueEventListener(chatValueListener)
    }

    private fun getChatId(receiverId: String) =
        if (auth.uid!! > receiverId) auth.uid!! + receiverId else receiverId + auth.uid;


}