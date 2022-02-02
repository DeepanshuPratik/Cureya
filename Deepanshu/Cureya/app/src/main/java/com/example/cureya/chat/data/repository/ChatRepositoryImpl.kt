package com.example.cureya.chat.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cureya.chat.data.models.Chat
import com.example.cureya.chat.data.models.ChatUser
import com.example.cureya.chat.data.models.Message
import com.example.cureya.chat.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatRepositoryImpl(
    private val database: DatabaseReference,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ChatRepository {

    private val userChat = MutableLiveData<Chat>()
    private val chatUsers = MutableLiveData<List<ChatUser>>()

    override suspend fun sendMessage(message: Message) {
        database.child("chats").child(getChatId(message.receiverId!!)).child("messages").push()
            .setValue(message).await()
        database.child("chat_users").child(auth.uid!!).child(message.receiverId)
            .child("lastMessage").setValue(message).await()
    }

    override suspend fun getChatByReceiverId(receiverId: String): LiveData<Chat> {
        database.child("users").child(auth.uid!!).child("chats").child(receiverId).setValue(" ")
            .await()
        listenForChatValueChanges(receiverId)
        return userChat
    }

    override suspend fun getAllChatUsers(): LiveData<List<ChatUser>> {
        listenForChatUsersValueChanges()
        return chatUsers
    }

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

    private fun listenForChatValueChanges(receiverId: String) {
        val chatValueListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val messages =
                        dataSnapshot.child("messages").children.map { it.getValue(Message::class.java)!! }
                    val chat = Chat(messages)
                    userChat.value = chat
                } else {
                    userChat.value = Chat()
                }
            }
        }
        database.child("chats").child(getChatId(receiverId))
            .addValueEventListener(chatValueListener)
    }

    private suspend fun getUserById(userId: String): User =
        database.child("users").child(userId).get().await().getValue(User::class.java)!!

    private fun getChatId(receiverId: String) =
        if (auth.uid!! > receiverId) auth.uid!! + receiverId else receiverId + auth.uid;

    companion object {
        private const val TAG = "CHAT_REPO"
    }

}