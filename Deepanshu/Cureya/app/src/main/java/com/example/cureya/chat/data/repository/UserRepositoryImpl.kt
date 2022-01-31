package com.example.cureya.chat.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cureya.chat.data.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val database: DatabaseReference
) : UserRepository {
    private val users = MutableLiveData<List<User>>()

    private fun listenForUsersValueChanges() {
        val userValueListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dbUsers = dataSnapshot.children
                        .mapNotNull { it.getValue(User::class.java) }.toList()
                    users.postValue(dbUsers)
                } else {
                    users.postValue(emptyList())
                }
            }
        }
        database.child("users").addValueEventListener(userValueListener)
    }

    override fun getAllUsers(): LiveData<List<User>> {
        listenForUsersValueChanges()
        return users
    }

    override suspend fun getUserById(userId: String): User? =
        database.child("users").child(userId).get().await().getValue(User::class.java)

}