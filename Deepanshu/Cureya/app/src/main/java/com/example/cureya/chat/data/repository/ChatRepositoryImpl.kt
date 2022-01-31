package com.example.cureya.chat.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cureya.chat.data.models.Chat
import com.example.cureya.chat.data.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import java.util.Date

class ChatRepositoryImpl(
    private val database: DatabaseReference,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ChatRepository {

    private val chats = MutableLiveData<List<Chat>>()
    private val chatIds = MutableLiveData<List<String>>()

    suspend fun sendMessage(message: Message) {
        database.child("chats").child(getChatId(message.receiverId!!)).child("messages").push()
            .setValue(message).await()
        database.child("chats").child(getChatId(message.receiverId)).child("updatedAt").setValue(Date())

    }

    private fun listenForChatsValueChanges() {
        val userValueListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dbUsers = dataSnapshot.children
                        .mapNotNull { it.getValue(Chat::class.java) }.toList()
                    chats.postValue(dbUsers)
                } else {
                    chats.postValue(emptyList())
                }
            }
        }
        database.child("users").child("chats").addValueEventListener(userValueListener)
    }

    override fun getChatById(chatId: String): LiveData<Chat> {
        TODO("Not yet implemented")
    }

//    override fun createChat() {
//
//    }


//    fun getAllChatIds(): LiveData<List<String>> {
//
//    }

    override fun getAllChats(): LiveData<List<Chat>> {
        listenForChatsValueChanges()
        return chats
    }

    // get the id for chat based on lexical comparison
    private fun getChatId(receiverId: String) =
        if (auth.uid!! > receiverId) auth.uid!! + receiverId else receiverId + auth.uid;

}